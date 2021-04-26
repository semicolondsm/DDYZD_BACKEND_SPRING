package com.semicolon.spring.service.feed;

import com.semicolon.spring.dto.FeedDTO.*;
import com.semicolon.spring.entity.club.Club;
import com.semicolon.spring.entity.club.ClubRepository;
import com.semicolon.spring.entity.club.club_member.ClubMemberRepository;
import com.semicolon.spring.entity.club.club_follow.ClubFollowRepository;
import com.semicolon.spring.entity.club.club_head.ClubHeadRepository;
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
    private String PATH;

    //Security Context에서 가져오는 User정보가 null이 아니라면 is follow와 isflag를 return한다. 만약 User정보가 null이라면 둘 다 false를 return한다.

    @Override
    public MessageResponse fileUpload(List<MultipartFile> files, int feedId) { // feed가 자기 클럽이 쓴것인지 확인.
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
                file.transferTo(new File(PATH + fileString));
                feedRepository.findById(feedId)
                        .map(feed -> feedMediumRepository.save(FeedMedium.builder()
                                .feed(feed)
                                .mediumPath("feed/" + fileString)
                                .build())
                        );

            }

            return new MessageResponse("File upload success.");
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileSaveFailException();
        }
    }

    @Override
    public WriteFeedResponse writeFeed(Feed request, int clubId) {
        if (isNotClubMember(clubId))
            throw new NotClubMemberException();

        return new WriteFeedResponse("feed writing success",
                feedRepository.save(
                        com.semicolon.spring.entity.feed.Feed.builder()
                                .contents(request.getContent())
                                .club(clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new))
                                .build()
                ).getId());
    }

    @Override
    public List<GetFeed> getFeedList(int page) {
        return feedToResponse(getFeeds(page).getContent());
    }

    @Override
    public List<GetFeedClub> getFeedClubList(int page, int clubId) {
        return feedClubToResponse(getFeedClub(page, clubId).getContent());
    }

    @Override
    public MessageResponse feedModify(Feed request, int feedId) { // feed를 쓴 클럽인지 확인절차 추가.
        Club club = feedRepository.findById(feedId).orElseThrow(FeedNotFoundException::new).getClub();
        if (isNotClubMember(club.getClubId()))
            throw new NotClubMemberException();
        feedRepository.findById(feedId)
                .map(feed -> {
                    feed.modify(request.getContent());
                    feedRepository.save(feed);
                    return feed;
                }).orElseThrow(FeedNotFoundException::new);

        return new MessageResponse("feed writing success");
    }

    @Override
    public MessageResponse feedFlag(int feedId) {
        User user = authenticationFacade.getUser();
        com.semicolon.spring.entity.feed.Feed feed = feedRepository.findById(feedId).orElseThrow(FeedNotFoundException::new);
        if (isFlag(user, feed)) {
            feedFlagRepository.delete(feedFlagRepository.findByUserAndFeed(user, feed).orElseThrow(BadRequestException::new));

            return new MessageResponse("Remove Feed Flag Success");
        } else {
            feedFlagRepository.save(
                    FeedFlag.builder()
                            .user(user)
                            .feed(feed)
                            .build()
            );

            return new MessageResponse("Add Feed Flag Success");
        }
    }

    @Override
    public GetFeed getFeed(int feedId) {
        User user = authenticationFacade.getUser();
        return feedRepository.findById(feedId)
                .map(feed -> getFeed(feed, user)).orElseThrow(FeedNotFoundException::new);
    }

    @Override
    public MessageResponse deleteFeed(int feedId) {
        com.semicolon.spring.entity.feed.Feed feed = feedRepository.findById(feedId).orElseThrow(FeedNotFoundException::new);
        if (isNotClubMember(feed.getClub().getClubId()))
            throw new NotClubMemberException();
        feedRepository.delete(feed);

        return new MessageResponse("Feed delete success.");
    }

    @Override
    public MessageResponse feedPin(int feedId) {
        com.semicolon.spring.entity.feed.Feed feed = feedRepository.findById(feedId).orElseThrow(FeedNotFoundException::new);

        if (!isNotClubHead(feed.getClub().getClubId())) {
            throw new NotClubHeadException();
        }
        if (!feed.isPin() && feedRepository.findByClubAndPinIsTrue(feed.getClub()).size() >= 1) {
            List<com.semicolon.spring.entity.feed.Feed> feedList = feedRepository.findByClubAndPinIsTrue(feed.getClub());
            for (com.semicolon.spring.entity.feed.Feed value : feedList) {
                value.changePin();
                feedRepository.save(value);
            }
        }

        feed.changePin();
        feedRepository.save(feed);

        return new MessageResponse("feed pin change success");
    }

    @Override
    public List<GetFeed> getFlagList(int page) {
        User user = authenticationFacade.getUser();
        Page<FeedFlag> flagList = feedFlagRepository.findByUser(user, PageRequest.of(page, 3, Sort.by("id").descending()));
        List<GetFeed> feedList = new ArrayList<>();
        for (FeedFlag flag : flagList) {
            com.semicolon.spring.entity.feed.Feed feed = flag.getFeed();
            feedList.add(GetFeed.builder()
                    .feedId(feed.getId())
                    .uploadAt(feed.getUploadAt())
                    .media(getMediaPath(feed.getMedia()))
                    .content(feed.getContents())
                    .profileImage(feed.getClub().getProfile_image())
                    .clubName(feed.getClub().getName())
                    .flags(feedFlagRepository.countByFeed(feed))
                    .isOwner(!isNotClubMember(feed.getClub().getClubId()))
                    .isFlag(isFlag(user, feed))
                    .isFollow(clubFollowRepository.findByUserAndClub(user, feed.getClub()).isPresent())
                    .build()
            );
        }

        return feedList;
    }

    @Override
    public List<UserResponse> getFeedUser(int feedId) {
        List<UserResponse> responses = new ArrayList<>();
        feedRepository.findById(feedId)
                .map(feed -> {
                    for (FeedFlag flag : feed.getFeedFlags()) {
                        responses.add(UserResponse.builder()
                                .userName(flag.getUser().getName())
                                .imagePath(flag.getUser().getImagePath())
                                .userId(flag.getUser().getId())
                                .build()
                        );
                    }
                    return responses;
                }).orElseThrow(FeedNotFoundException::new);
        return responses;
    }

    private boolean isFlag(User user, com.semicolon.spring.entity.feed.Feed feed) {
        if (user != null)
            return feedFlagRepository.findByUserAndFeed(user, feed).isPresent();
        else throw new UserNotFoundException();
    }

    public List<GetFeed> feedToResponse(List<com.semicolon.spring.entity.feed.Feed> feeds) { // 유저 정보가 있을 때 isFlag, isFollow
        List<GetFeed> response = new ArrayList<>();
        User user = authenticationFacade.getUser();
        for (com.semicolon.spring.entity.feed.Feed feed : feeds) {
            response.add(getFeed(feed, user));
        }

        return response;
    }

    public List<GetFeedClub> feedClubToResponse(List<com.semicolon.spring.entity.feed.Feed> feeds) { // 유저 정보가 있을 때 isFlag, isFollow
        List<GetFeedClub> response = new ArrayList<>();
        User user = authenticationFacade.getUser();
        for (com.semicolon.spring.entity.feed.Feed feed : feeds) {
            GetFeedClub getFeedClub = GetFeedClub.builder()
                    .feedId(feed.getId())
                    .clubName(feed.getClub().getName())
                    .profileImage(feed.getClub().getProfile_image())
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
        PageRequest pageRequest = PageRequest.of(page, 3, Sort.by("uploadAt").descending());
        return feedRepository.findAll(pageRequest);
    }

    public Page<com.semicolon.spring.entity.feed.Feed> getFeedClub(int page, int clubId) {
        Club club = clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new);
        PageRequest pageRequest = PageRequest.of(page, 3, Sort.by("pin").descending().and(Sort.by("uploadAt").descending()));
        return feedRepository.findByClub(club, pageRequest);
    }

    private boolean isNotClubMember(int clubId) { // user가 속해있지 않은 club_id를 보내는 테스트 해야함.
        Club club = clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new);

        return clubMemberRepository.findByUserAndClub(authenticationFacade.getUser(), club).isEmpty();
    }

    private boolean isNotClubHead(int clubId) {
        User user = authenticationFacade.getUser();
        Club club = clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new);
        clubHeadRepository.findByClubAndUser(club, user).orElseThrow(NotClubHeadException::new);
        return false;
    }

    private GetFeed getFeed(com.semicolon.spring.entity.feed.Feed feed, User user) {
        GetFeed getFeed = GetFeed.builder()
                .feedId(feed.getId())
                .clubName(feed.getClub().getName())
                .clubId(feed.getClub().getClubId())
                .profileImage(feed.getClub().getProfile_image())
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
