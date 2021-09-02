package com.semicolon.spring.service.feed;

import com.semicolon.spring.dto.feed.request.ContentRequest;
import com.semicolon.spring.dto.feed.response.GetFeedClubResponse;
import com.semicolon.spring.dto.feed.response.GetFeedResponse;
import com.semicolon.spring.dto.user.response.UserInfoResponse;
import com.semicolon.spring.entity.club.Club;
import com.semicolon.spring.entity.club.ClubRepository;
import com.semicolon.spring.entity.club.club_follow.ClubFollowRepository;
import com.semicolon.spring.entity.club.club_head.ClubHeadRepository;
import com.semicolon.spring.entity.club.club_member.ClubMemberRepository;
import com.semicolon.spring.entity.feed.Feed;
import com.semicolon.spring.entity.feed.FeedRepository;
import com.semicolon.spring.entity.feed.MediaComparator;
import com.semicolon.spring.entity.feed.feed_flag.FeedFlag;
import com.semicolon.spring.entity.feed.feed_flag.FeedFlagRepository;
import com.semicolon.spring.entity.feed.feed_medium.FeedMedium;
import com.semicolon.spring.entity.feed.feed_medium.FeedMediumRepository;
import com.semicolon.spring.entity.user.User;
import com.semicolon.spring.exception.*;
import com.semicolon.spring.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedServiceImpl implements FeedService {
    private final FeedRepository feedRepository;
    private final FeedMediumRepository feedMediumRepository;
    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final FeedFlagRepository feedFlagRepository;
    private final ClubFollowRepository clubFollowRepository;
    private final ClubHeadRepository clubHeadRepository;
    private final AuthenticationFacade authenticationFacade;

    @Value("${file.path}")
    private String path;


    @Override
    public void fileUpload(List<MultipartFile> files, int feedId) {
        if (isNotClubMember(feedRepository.findById(feedId).orElseThrow(FeedNotFoundException::new).getClub().getClubId()))
            throw new NotClubMemberException();
        try {
            for (MultipartFile file : files) {
                if (file.getOriginalFilename() == null || file.getOriginalFilename().length() == 0)
                    throw new FileNotFoundException();
                int index = file.getOriginalFilename().lastIndexOf(".");
                String extension = file.getOriginalFilename().substring(index + 1);

                if (!(extension.contains("jpg") || extension.contains("HEIC") || extension.contains("jpeg") || extension.contains("png") || extension.contains("heic"))) {
                    throw new BadFileExtensionException();
                }

                String fileString = UUID.randomUUID().toString() + "." + extension;
                file.transferTo(new File(path + fileString));
                feedRepository.findById(feedId)
                        .map(feed -> feedMediumRepository.save(FeedMedium.builder()
                                .feed(feed)
                                .mediumPath("feed/" + fileString)
                                .build())
                        );

            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new FileSaveFailException();
        }
    }

    @Override
    public void writeFeed(ContentRequest request, int clubId) {
        if (isNotClubMember(clubId))
            throw new NotClubMemberException();

        feedRepository.save(
                Feed.builder()
                        .contents(request.getContents())
                        .club(clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new))
                        .build());
    }

    @Override
    public List<GetFeedResponse> getFeedList(int page) {
        return feedToResponse(getFeeds(page).getContent());
    }

    @Override
    public List<GetFeedClubResponse> getFeedClubList(int page, int clubId) {
        return feedClubToResponse(getFeedClub(page, clubId).getContent());
    }

    @Override
    public void feedModify(ContentRequest request, int feedId) {
        Club club = feedRepository.findById(feedId).orElseThrow(FeedNotFoundException::new).getClub();
        if (isNotClubMember(club.getClubId()))
            throw new NotClubMemberException();
        feedRepository.findById(feedId)
                .map(feed -> {
                    feed.modify(request.getContents());
                    feedRepository.save(feed);
                    return feed;
                }).orElseThrow(FeedNotFoundException::new);
    }

    @Override
    public void feedFlag(int feedId) {
        User user = authenticationFacade.getUser();
        Feed feed = feedRepository.findById(feedId).orElseThrow(FeedNotFoundException::new);
        if (isFlag(user, feed)) {
            feedFlagRepository.delete(feedFlagRepository.findByUserAndFeed(user, feed).orElseThrow(BadRequestException::new));

        } else {
            feedFlagRepository.save(
                    FeedFlag.builder()
                            .user(user)
                            .feed(feed)
                            .build()
            );

        }
    }

    @Override
    public GetFeedResponse getFeed(int feedId) {
        User user = authenticationFacade.getUser();
        return feedRepository.findById(feedId)
                .map(feed -> getFeed(feed, user)).orElseThrow(FeedNotFoundException::new);
    }

    @Override
    public void deleteFeed(int feedId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(FeedNotFoundException::new);
        if (isNotClubMember(feed.getClub().getClubId()))
            throw new NotClubMemberException();
        feedRepository.delete(feed);
    }

    @Override
    public void feedPin(int feedId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(FeedNotFoundException::new);


        isNotClubHead(feed.getClub().getClubId());

        if (!feed.isPin() && !feedRepository.findByClubAndPinIsTrue(feed.getClub()).isEmpty()) {
            List<com.semicolon.spring.entity.feed.Feed> feedList = feedRepository.findByClubAndPinIsTrue(feed.getClub());
            for (com.semicolon.spring.entity.feed.Feed value : feedList) {
                value.changePin();
                feedRepository.save(value);
            }
        }

        feed.changePin();
        feedRepository.save(feed);

    }

    @Override
    public List<GetFeedResponse> getFlagList(int page) {
        User user = authenticationFacade.getUser();
        Page<FeedFlag> flagList = feedFlagRepository
                .findByUser(user, PageRequest.of(page, 3, Sort.by("id").descending()));
        return flagList.stream()
                .map(flag -> {
                    Feed feed = flag.getFeed();
                    return GetFeedResponse.builder()
                            .feedId(feed.getId())
                            .uploadAt(feed.getUploadAt())
                            .media(getMediaPath(feed.getMedia()))
                            .content(feed.getContents())
                            .profileImage(feed.getClub().getProfileImage())
                            .clubName(feed.getClub().getName())
                            .flags(feedFlagRepository.countByFeed(feed))
                            .isOwner(!isNotClubMember(feed.getClub().getClubId()))
                            .isFlag(isFlag(user, feed))
                            .isFollow(clubFollowRepository.findByUserAndClub(user, feed.getClub()).isPresent())
                            .build();
                }).collect(Collectors.toList());
    }

    @Override
    public List<UserInfoResponse> getFeedUser(int feedId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(FeedNotFoundException::new);

        return feed.getFeedFlags().stream()
                .map(flag -> UserInfoResponse.builder()
                        .userName(flag.getUser().getName())
                        .imagePath(flag.getUser().getImagePath())
                        .userId(flag.getUser().getId())
                        .build())
                .collect(Collectors.toList());
    }

    private boolean isFlag(User user, com.semicolon.spring.entity.feed.Feed feed) {
        if (user != null)
            return feedFlagRepository.findByUserAndFeed(user, feed).isPresent();
        else throw new UserNotFoundException();
    }

    public List<GetFeedResponse> feedToResponse(List<Feed> feeds) {

        User user = authenticationFacade.getUser();

        return feeds.stream()
                .map(feed -> getFeed(feed, user))
                .collect(Collectors.toList());
    }

    public List<GetFeedClubResponse> feedClubToResponse(List<com.semicolon.spring.entity.feed.Feed> feeds) {
        List<GetFeedClubResponse> response = new ArrayList<>();
        User user = authenticationFacade.getUser();
        for (com.semicolon.spring.entity.feed.Feed feed : feeds) {
            GetFeedClubResponse getFeedClub = GetFeedClubResponse.builder()
                    .feedId(feed.getId())
                    .clubName(feed.getClub().getName())
                    .profileImage(feed.getClub().getProfileImage())
                    .content(feed.getContents())
                    .media(getMediaPath(feed.getMedia()))
                    .uploadAt(feed.getUploadAt())
                    .isPin(feed.isPin())
                    .flags(feedFlagRepository.countByFeed(feed))
                    .build();
            if (user != null) {
                getFeedClub.setIsFlag(isFlag(user, feed));
                getFeedClub.setIsFollow(clubFollowRepository.findByUserAndClub(user, feed.getClub()).isPresent());
                getFeedClub.setOwner(!isNotClubMember(feed.getClub().getClubId()));
            }
            response.add(getFeedClub);
        }

        return response;
    }

    public List<String> getMediaPath(Set<FeedMedium> feedMedia) {
        List<FeedMedium> list = new ArrayList<>(feedMedia);
        List<String> response = new ArrayList<>();
        Collections.sort(list, new MediaComparator());
        for (FeedMedium feedMedium : list) {
            response.add(feedMedium.getMediumPath());
        }
        return response;
    }

    public Page<com.semicolon.spring.entity.feed.Feed> getFeeds(int page) {
        PageRequest pageRequest = PageRequest
                .of(page, 3, Sort.by("uploadAt").descending());
        return feedRepository.findAll(pageRequest);
    }

    public Page<com.semicolon.spring.entity.feed.Feed> getFeedClub(int page, int clubId) {
        Club club = clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new);
        PageRequest pageRequest = PageRequest.of(page, 3,
                Sort.by("pin").descending().and(Sort.by("uploadAt").descending()));
        return feedRepository.findByClub(club, pageRequest);
    }

    private boolean isNotClubMember(int clubId) {
        Club club = clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new);

        return clubMemberRepository.findByUserAndClub(authenticationFacade.getUser(), club).isEmpty();
    }

    private void isNotClubHead(int clubId) {
        User user = authenticationFacade.getUser();
        Club club = clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new);
        clubHeadRepository.findByClubAndUser(club, user).orElseThrow(NotClubHeadException::new);
    }

    private GetFeedResponse getFeed(com.semicolon.spring.entity.feed.Feed feed, User user) {
        GetFeedResponse getFeed = GetFeedResponse.builder()
                .feedId(feed.getId())
                .clubName(feed.getClub().getName())
                .clubId(feed.getClub().getClubId())
                .profileImage(feed.getClub().getProfileImage())
                .content(feed.getContents())
                .media(getMediaPath(feed.getMedia()))
                .uploadAt(feed.getUploadAt())
                .flags(feedFlagRepository.countByFeed(feed))
                .build();
        if (user != null) {
            getFeed.setIsFlag(isFlag(user, feed));
            getFeed.setIsFollow(clubFollowRepository.findByUserAndClub(user, feed.getClub()).isPresent());
            getFeed.setOwner(!isNotClubMember(feed.getClub().getClubId()));
        }
        return getFeed;
    }

}
