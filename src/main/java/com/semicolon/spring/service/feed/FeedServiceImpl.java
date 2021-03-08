package com.semicolon.spring.service.feed;

import com.semicolon.spring.dto.FeedDTO;
import com.semicolon.spring.entity.club.Club;
import com.semicolon.spring.entity.club.ClubRepository;
import com.semicolon.spring.entity.club.club_member.ClubMemberRepository;
import com.semicolon.spring.entity.club.club_follow.ClubFollowRepository;
import com.semicolon.spring.entity.club.club_head.ClubHead;
import com.semicolon.spring.entity.club.club_head.ClubHeadRepository;
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
import com.semicolon.spring.security.jwt.auth.AuthDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedServiceImpl implements FeedService{
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
    public FeedDTO.messageResponse fileUpload(List<MultipartFile> files, int feedId) { // feed가 자기 클럽이 쓴것인지 확인.
        if(isNotClubMember(feedRepository.findById(feedId).orElseThrow(FeedNotFoundException::new).getClub().getClubId()))
            throw new NotClubMemberException();
        try{
            for(MultipartFile file : files){
                if(file.getOriginalFilename()==null||file.getOriginalFilename().length()==0)
                    throw new FileNotFoundException();
                int index = file.getOriginalFilename().lastIndexOf(".");
                String extension = file.getOriginalFilename().substring(index + 1);

                if(!(extension.contains("jpg")||extension.contains("HEIC")||extension.contains("jpeg")||extension.contains("png")||extension.contains("heic"))){
                    throw new BadFileExtensionException();
                }

                String fileString = UUID.randomUUID().toString()+"."+extension;
                file.transferTo(new File(PATH + fileString));
                feedRepository.findById(feedId)
                        .map(feed-> feedMediumRepository.save(FeedMedium.builder()
                                .feed(feed)
                                .medium_path("feed/" + fileString)
                                .build())
                        );
                log.info("file_name : " + fileString + ", feed_id : " + feedId);
            }
            log.info("fileUpload feed_id : " + feedId);
            return new FeedDTO.messageResponse("File upload success.");
        }catch (IOException e){
            e.printStackTrace();
            throw new FileSaveFailException();
        }
    }

    @Override
    public FeedDTO.writeFeedResponse writeFeed(FeedDTO.feed request, int club_id) {
        if(isNotClubMember(club_id))
            throw new NotClubMemberException();
        log.info("writeFeed club_id : " + club_id);
        return new FeedDTO.writeFeedResponse("feed writing success",
                feedRepository.save(
                    Feed.builder()
                        .contents(request.getContent())
                        .club(clubRepository.findByClubId(club_id))
                        .build()
                ).getId());
    }

    @Override
    public List<FeedDTO.getFeed> getFeedList(int page) {
        return feedToResponse(getFeeds(page).getContent(), page);
    }

    @Override
    public List<FeedDTO.getFeedClub> getFeedClubList(int page, int club_id) {
        return feedClubToResponse(getFeedClub(page, club_id).getContent(), page);
    }

    @Override
    public FeedDTO.messageResponse feedModify(FeedDTO.feed request, int feedId) { // feed를 쓴 클럽인지 확인절차 추가.
        Club club = feedRepository.findById(feedId).orElseThrow(FeedNotFoundException::new).getClub();
        if(isNotClubMember(club.getClubId()))
            throw new NotClubMemberException();
        feedRepository.findById(feedId)
                .map(feed -> {
                    feed.modify(request.getContent());
                    feedRepository.save(feed);
                    return feed;
                }).orElseThrow(FeedNotFoundException::new);
        log.info("feedModify feed_id : " + feedId);
        return new FeedDTO.messageResponse("feed writing success");
    }

    @Override
    public FeedDTO.messageResponse feedFlag(int feedId) {
        try{
            User user = authenticationFacade.getUser();
            Feed feed = feedRepository.findById(feedId).orElseThrow(FeedNotFoundException::new);
            if(isFlag(user, feed)){
                feedFlagRepository.delete(feedFlagRepository.findByUserAndFeed(user, feed).orElseThrow(BadRequestException::new));
                log.info("Remove Feed Flag user_id : " + user.getId());
                return new FeedDTO.messageResponse("Remove Feed Flag Success");
            }else{
                feedFlagRepository.save(
                        FeedFlag.builder()
                                .user(user)
                                .feed(feed)
                                .build()
                );
                log.info("Add Feed Flag user_id : " + user.getId());
                return new FeedDTO.messageResponse("Add Feed Flag Success");
            }
        }catch (Exception e){
            throw new BadRequestException();
        }


    }

    @Override
    public FeedDTO.getFeed getFeed(int feedId) {
        User user = authenticationFacade.getUser();
        return feedRepository.findById(feedId)
                .map(feed -> {
                    FeedDTO.getFeed getFeed = FeedDTO.getFeed.builder()
                            .feedId(feed.getId())
                            .clubName(feed.getClub().getName())
                            .profileImage(feed.getClub().getProfile_image())
                            .content(feed.getContents())
                            .media(getMediaPath(feed.getMedia()))
                            .uploadAt(feed.getUploadAt())
                            .flags(feedFlagRepository.countByFeed(feed))
                            .build();
                    if(user!=null){
                        getFeed.setIsFlag(isFlag(user, feed));
                        getFeed.setIsFollow(clubFollowRepository.findByUserAndClub(user, feed.getClub()).isPresent());
                        getFeed.setOwner(!isNotClubMember(feed.getClub().getClubId()));
                    }
                    log.info("getFeed feedId : " + feedId);
                    return getFeed;
                }).orElseThrow(FeedNotFoundException::new);
    }

    @Override
    public FeedDTO.messageResponse deleteFeed(int feedId) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(FeedNotFoundException::new);
        if(isNotClubMember(feed.getClub().getClubId()))
            throw new NotClubMemberException();
        feedRepository.delete(feed);
        log.info("deleteFeed feedId : " + feedId);
        return new FeedDTO.messageResponse("Feed delete success.");
    }

    @Override
    public FeedDTO.messageResponse feedPin(int feedId) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(FeedNotFoundException::new);

        if(!isClubHead(feed.getClub().getClubId())){
            throw new NotClubHeadException();
        }
        if(!feed.isPin()&&feedRepository.findByClubAndPinIsTrue(feed.getClub()).size()>=1){
            List<Feed> feedList = feedRepository.findByClubAndPinIsTrue(feed.getClub());
            for(Feed value : feedList){
                value.changePin();
                feedRepository.save(value);
            }
        }

        feed.changePin();
        feedRepository.save(feed);
        log.info("feed pin change success feedId : " + feedId);

        return new FeedDTO.messageResponse("feed pin change success");
    }

    @Override
    public List<FeedDTO.getFeed> getFeedList() {
        User user = authenticationFacade.getUser();
        List<FeedDTO.getFeed> feedList = new ArrayList<>();
        for(FeedFlag flag : user.getFeedFlags()){
            Feed feed = flag.getFeed();
            feedList.add(FeedDTO.getFeed.builder()
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
    public List<FeedDTO.userResponse> getFeedUser(int feedId) {
        List<FeedDTO.userResponse> responses = new ArrayList<>();
        feedRepository.findById(feedId)
                .map(feed -> {
                    for(FeedFlag flag : feed.getFeedFlags()){
                        responses.add(FeedDTO.userResponse.builder()
                                .userName(flag.getUser().getName())
                                .imagePath(flag.getUser().getImage_path())
                                .userId(flag.getUser().getId())
                                .build()
                        );
                    }
                    return responses;
                }).orElseThrow(FeedNotFoundException::new);
        return responses;
    }

    private boolean isFlag(User user, Feed feed){
        if(user!=null)
            return feedFlagRepository.findByUserAndFeed(user, feed).isPresent();
        else throw new UserNotFoundException();
    }

    public List<FeedDTO.getFeed> feedToResponse(List<Feed> feeds, int page){ // 유저 정보가 있을 때 isFlag, isFollow
        log.info("method" + String.valueOf(new Date()));
        List<FeedDTO.getFeed> response = new ArrayList<>();
        User user = authenticationFacade.getUser();
        for(Feed feed : feeds){log.info(String.valueOf(new Date()));
            FeedDTO.getFeed getFeed = FeedDTO.getFeed.builder()
                    .feedId(feed.getId())
                    .clubName(feed.getClub().getName())
                    .profileImage(feed.getClub().getProfile_image())
                    .content(feed.getContents())
                    .media(getMediaPath(feed.getMedia()))
                    .uploadAt(feed.getUploadAt())
                    .flags(feedFlagRepository.countByFeed(feed))
                    .build();

            if(user!=null){
                getFeed.setIsFlag(isFlag(user, feed));
                getFeed.setIsFollow(clubFollowRepository.findByUserAndClub(user, feed.getClub()).isPresent());
                getFeed.setOwner(!isNotClubMember(feed.getClub().getClubId()));
            }
            response.add(getFeed);
        }
        log.info("get feedList page : " + page);
        log.info(String.valueOf(new Date()));
        return response;
    }

    public List<FeedDTO.getFeedClub> feedClubToResponse(List<Feed> feeds, int page){ // 유저 정보가 있을 때 isFlag, isFollow
        List<FeedDTO.getFeedClub> response = new ArrayList<>();
        User user = authenticationFacade.getUser();
        for(Feed feed : feeds){
            FeedDTO.getFeedClub getFeedClub = FeedDTO.getFeedClub.builder()
                    .feedId(feed.getId())
                    .clubName(feed.getClub().getName())
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
                getFeedClub.setOwner(!isNotClubMember(feed.getClub().getClubId()));
            }
            response.add(getFeedClub);
        }
        log.info("get feedClubList page : " + page);
        return response;
    }

    public List<String> getMediaPath(Set<FeedMedium> feedMedia){
        List<FeedMedium> list = new ArrayList<>(feedMedia);
        List<String> response = new ArrayList<>();
        Collections.sort(list, new MediaComparator());
        for(FeedMedium feedMedium : list){
            response.add(feedMedium.getMedium_path());
        }
        return response;
    }

    public Page<Feed> getFeeds(int page){
        PageRequest pageRequest = PageRequest.of(page, 3);
        Page<Feed> feeds = feedRepository.findAllByOrderByIdDesc(pageRequest);
        return feedRepository.findAll(pageRequest);
    }

    public Page<Feed> getFeedClub(int page, int club_id){
        Club club = clubRepository.findById(club_id).orElseThrow(ClubNotFoundException::new);
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("pin").descending().and(Sort.by("uploadAt").descending()));
        return feedRepository.findByClub(club, pageRequest);
    }

    private boolean isNotClubMember(int club_id){ // user가 속해있지 않은 club_id를 보내는 테스트 해야함.
        Club club = clubRepository.findById(club_id).orElseThrow(ClubNotFoundException::new);
        //log.info("isNotClubMember user_name : " + authenticationFacade.getUser().getName());
        return !clubMemberRepository.findByUserAndClub(authenticationFacade.getUser(), club).isPresent();
    }

    private boolean isClubHead(int club_id){
        User user = authenticationFacade.getUser();
        Club club = clubRepository.findById(club_id).orElseThrow(ClubNotFoundException::new);
        ClubHead clubHead = clubHeadRepository.findByClubAndUser(club, user);
        if(clubHead == null)
            throw new NotClubHeadException();
        else return true;
    }
}
