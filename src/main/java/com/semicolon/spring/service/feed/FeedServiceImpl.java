package com.semicolon.spring.service.feed;

import com.semicolon.spring.dto.FeedDTO;
import com.semicolon.spring.entity.club.Club;
import com.semicolon.spring.entity.club.ClubRepository;
import com.semicolon.spring.entity.feed.Feed;
import com.semicolon.spring.entity.feed.FeedRepository;
import com.semicolon.spring.entity.feed_medium.FeedMedium;
import com.semicolon.spring.entity.feed_medium.FeedMediumRepository;
import com.semicolon.spring.exception.ClubNotExistException;
import com.semicolon.spring.exception.FeedNotExistException;
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

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService{
    private final FeedRepository feedRepository;
    private final FeedMediumRepository feedMediumRepository;
    private final ClubRepository clubRepository;

    @Value("${file.path}")
    private String PATH;

    @Override
    public void fileUpload(MultipartFile file, int feedId) { // feed가 자기 클럽이 쓴것인지 확인.
        try{
            file.transferTo(new File(PATH+file.getOriginalFilename()));
            feedRepository.findById(feedId)
                    .map(feed-> feedMediumRepository.save(FeedMedium.builder()
                            .feed_id(feed)
                            .medium_path(PATH+file.getOriginalFilename())
                            .build())
                    );
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public FeedDTO.writeFeedResponse writeFeed(FeedDTO.feed request) {
        return new FeedDTO.writeFeedResponse("feed writing success",
                feedRepository.save(
                    Feed.builder()
                        .contents(request.getContent())
                        .pin(request.isPin())
                        .clubId(clubRepository.findById(1).orElseThrow(ClubNotExistException::new)) // 차후에 수정 필요
                        .flag(0)
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
        feedRepository.findById(feedId)
                .map(feed -> {
                    feed.modify(request.getContent(), request.isPin());
                    feedRepository.save(feed);
                    return feed;
                }).orElseThrow(FeedNotExistException::new);
        return new FeedDTO.messageResponse("feed writing success");
    }

    @Override
    public FeedDTO.messageResponse addFeedFlag(int feedId) { //User가 flag를 달았을 시 feed_flag에 추가, feed_flag에 있는지 확인 추가.
        feedRepository.findById(feedId)
                .map(feed -> {
                    feed.addFlag();
                    feedRepository.save(feed);
                    return feed;
                });
        return new FeedDTO.messageResponse("Success");
    }

    @Override
    public FeedDTO.messageResponse deleteFeedFlag(int feedId) {
        feedRepository.findById(feedId)
                .map(feed -> {
                    feed.deleteFlag();
                    feedRepository.save(feed);
                    return feed;
                });
        return new FeedDTO.messageResponse("Success");
    }

    public List<FeedDTO.getFeed> feedToRepose(List<Feed> feeds){
        List<FeedDTO.getFeed> response = new ArrayList<>();
        for(Feed feed : feeds){
            response.add(
                    FeedDTO.getFeed.builder()
                    .feedId(feed.getId())
                    .clubName(feed.getClubId().getClub_name())
                    .profileImage(feed.getClubId().getProfile_image())
                    .content(feed.getContents())
                    .media(getMediaPath(feed.getMedia()))
                    .uploadAt(feed.getUploadAt())
                    .flags(feed.getFlag())
                    .build()
            );
        }
        return response;
    }

    public List<FeedDTO.getFeedClub> feedClubToRepose(List<Feed> feeds){
        List<FeedDTO.getFeedClub> response = new ArrayList<>();
        for(Feed feed : feeds){
            response.add(
                    FeedDTO.getFeedClub.builder()
                            .feedId(feed.getId())
                            .clubName(feed.getClubId().getClub_name())
                            .profileImage(feed.getClubId().getProfile_image())
                            .content(feed.getContents())
                            .media(getMediaPath(feed.getMedia()))
                            .uploadAt(feed.getUploadAt())
                            .isPin(feed.isPin())
                            .flags(feed.getFlag())
                            .build()
            );
        }
        return response;
    }

    public List<String> getMediaPath(List<FeedMedium> feedMedia){
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
        return feedRepository.findByClubId(club, pageRequest);
    }
}
