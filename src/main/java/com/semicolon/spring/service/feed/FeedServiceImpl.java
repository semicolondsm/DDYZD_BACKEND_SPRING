package com.semicolon.spring.service.feed;

import com.semicolon.spring.dto.FeedDTO;
import com.semicolon.spring.entity.club.Club;
import com.semicolon.spring.entity.club.ClubRepository;
import com.semicolon.spring.entity.club.application.ApplicationRepository;
import com.semicolon.spring.entity.club.club_follow.ClubFollowRepository;
import com.semicolon.spring.entity.club.club_head.ClubHeadRepository;
import com.semicolon.spring.entity.feed.Feed;
import com.semicolon.spring.entity.feed.FeedRepository;
import com.semicolon.spring.entity.feed.feed_flag.FeedFlag;
import com.semicolon.spring.entity.feed.feed_flag.FeedFlagRepository;
import com.semicolon.spring.entity.feed.feed_medium.FeedMedium;
import com.semicolon.spring.entity.feed.feed_medium.FeedMediumRepository;
import com.semicolon.spring.entity.user.User;
import com.semicolon.spring.exception.BadRequestException;
import com.semicolon.spring.exception.ClubNotExistException;
import com.semicolon.spring.exception.FeedNotExistException;
import com.semicolon.spring.exception.NoAuthorityException;
import com.semicolon.spring.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService{
    private final FeedRepository feedRepository;
    private final FeedMediumRepository feedMediumRepository;
    private final ClubRepository clubRepository;
    private final ApplicationRepository applicationRepository;
    private final FeedFlagRepository feedFlagRepository;
    private final ClubFollowRepository clubFollowRepository;
    private final ClubHeadRepository clubHeadRepository;
    private final AuthenticationFacade authenticationFacade;

    @Value("${file.path}")
    private String PATH;

    //Security Context에서 가져오는 User정보가 null이 아니라면 is follow와 isflag를 return한다. 만약 User정보가 null이라면 둘 다 false를 return한다.

    @Override
    public void fileUpload(MultipartFile file, int feedId) { // feed가 자기 클럽이 쓴것인지 확인.
        if(isNotClubMember(feedRepository.findById(feedId).orElseThrow(FeedNotExistException::new).getClub().getClubId()))
            throw new NoAuthorityException();
        try{
            file.transferTo(new File(PATH+file.getOriginalFilename()));
            feedRepository.findById(feedId)
                    .map(feed-> feedMediumRepository.save(FeedMedium.builder()
                            .feed(feed)
                            .medium_path(PATH+file.getOriginalFilename())
                            .build())
                    );
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public FeedDTO.writeFeedResponse writeFeed(FeedDTO.feed request, int club_id) {
        if(isNotClubMember(club_id))
            throw new NoAuthorityException();
        return new FeedDTO.writeFeedResponse("feed writing success",
                feedRepository.save(
                    Feed.builder()
                        .contents(request.getContent())
                        .pin(request.isPin())
                        //.club(clubRepository.findById(1).orElseThrow(ClubNotExistException::new)) // 차후에 수정 필요
                        .club(clubRepository.findByClubId(club_id))
                        .build()
                ).getId());

        }

    @Override
    public List<FeedDTO.getFeed> getFeedList(int page) {
        return feedToRepose(getFeed(page).getContent());
    }

    @Override
    public List<FeedDTO.getFeedClub> getFeedClubList(int page, int club_id) {
        return feedClubToRepose(getFeedClub(page, club_id).getContent());
    }

    @Override
    public FeedDTO.messageResponse feedModify(FeedDTO.feed request, int feedId) { // feed를 쓴 클럽인지 확인절차 추가.
        if(isNotClubMember(feedRepository.findById(feedId).orElseThrow(FeedNotExistException::new).getClub().getClubId()))
            throw new NoAuthorityException();
        feedRepository.findById(feedId)
                .map(feed -> {
                    feed.modify(request.getContent(), request.isPin());
                    feedRepository.save(feed);
                    return feed;
                }).orElseThrow(FeedNotExistException::new);
        return new FeedDTO.messageResponse("feed writing success");
    }

    @Override
    public FeedDTO.messageResponse feedFlag(int feedId) {
        User user = authenticationFacade.getUser();
        Feed feed = feedRepository.findById(feedId).orElseThrow(FeedNotExistException::new);
        if(isFlag(user, feed)){
            feedFlagRepository.delete(feedFlagRepository.findByUserAndFeed(user, feed).orElseThrow(BadRequestException::new));
            return new FeedDTO.messageResponse("Remove Feed Flag Success");
        }else{
            feedFlagRepository.save(
                    FeedFlag.builder()
                    .user(user)
                    .feed(feed)
                    .build()
            );
            return new FeedDTO.messageResponse("Add Feed Flag Success");
        }

    }

    private boolean isFlag(User user, Feed feed){
        if(user!=null)
            return feedFlagRepository.findByUserAndFeed(user, feed).isPresent();
        else throw new NoAuthorityException();
    }

    public List<FeedDTO.getFeed> feedToRepose(List<Feed> feeds){ // 유저 정보가 있을 때 isFlag, isFollow
        List<FeedDTO.getFeed> response = new ArrayList<>();
        User user = authenticationFacade.getUser();
        for(Feed feed : feeds){
            FeedDTO.getFeed getFeed = FeedDTO.getFeed.builder()
                    .feedId(feed.getId())
                    .clubName(feed.getClub().getClub_name())
                    .profileImage(feed.getClub().getProfile_image())
                    .content(feed.getContents())
                    .media(getMediaPath(feed.getMedia()))
                    .uploadAt(feed.getUploadAt())
                    .flags(feedFlagRepository.countByFeed(feed))
                    .build();

            if(user!=null){
                getFeed.setIsFlag(isFlag(user, feed));
                getFeed.setIsFollow(clubFollowRepository.findByUserAndClub(user, feed.getClub()).isPresent());
            }
            response.add(getFeed);
        }
        return response;
    }

    public List<FeedDTO.getFeedClub> feedClubToRepose(List<Feed> feeds){ // 유저 정보가 있을 때 isFlag, isFollow
        List<FeedDTO.getFeedClub> response = new ArrayList<>();
        User user = authenticationFacade.getUser();
        for(Feed feed : feeds){
            FeedDTO.getFeedClub getFeedClub = FeedDTO.getFeedClub.builder()
                    .feedId(feed.getId())
                    .clubName(feed.getClub().getClub_name())
                    .profileImage(feed.getClub().getProfile_image())
                    .content(feed.getContents())
                    .media(getMediaPath(feed.getMedia()))
                    .uploadAt(feed.getUploadAt())
                    .isPin(feed.isPin())
                    .flags(feedFlagRepository.countByFeed(feed))
                    .build();
            if(user!=null){
                getFeedClub.setIsFlag(isFlag(user, feed));
                getFeedClub.setIsFollow(clubFollowRepository.findByUserAndClub(user, feed.getClub()).isPresent());
            }
            response.add(getFeedClub);
        }
        return response;
    }

    public List<String> getMediaPath(Set<FeedMedium> feedMedia){
        List<String> response = new ArrayList<>();
        for(FeedMedium feedMedium : feedMedia){
            response.add(feedMedium.getMedium_path());
        }
        return response;
    }

    public Page<Feed> getFeed(int page){
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("uploadAt").descending());
        return feedRepository.findAll(pageRequest);
    }

    public Page<Feed> getFeedClub(int page, int club_id){
        Club club = clubRepository.findById(club_id).orElseThrow(ClubNotExistException::new);
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("pin").descending().and(Sort.by("uploadAt").descending()));
        return feedRepository.findByClub(club, pageRequest);
    }

    private boolean isNotClubMember(int club_id){ // user가 속해있지 않은 club_id를 보내는 테스트 해야함.
        User user = authenticationFacade.getUser();
        Club club = clubRepository.findByClubId(club_id);
        if(user == null || club == null)
            throw new BadRequestException();
        return applicationRepository.findByUserAndClub(user, club) == null && !user.getHead().contains(club.getClubHead());
    }
}
