package com.semicolon.spring.service.feed;

import com.semicolon.spring.dto.FeedDTO;
import com.semicolon.spring.entity.club.Club;
import com.semicolon.spring.entity.club.ClubRepository;
import com.semicolon.spring.entity.feed.Feed;
import com.semicolon.spring.entity.feed.FeedRepository;
import com.semicolon.spring.entity.feed_medium.FeedMedium;
import com.semicolon.spring.entity.feed_medium.FeedMediumRepository;
import lombok.RequiredArgsConstructor;
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

    @Override
    public void fileUpload(MultipartFile file, int feedId) {
        try{
            file.transferTo(new File("C:/Semicolon/DDYZD_V2_BACKEND_SPRING/"+file.getOriginalFilename()));
            feedRepository.findById(feedId)
                    .map(feed-> feedMediumRepository.save(FeedMedium.builder()
                            .feed_id(feed)
                            .medium_path("C:/Semicolon/DDYZD_V2_BACKEND_SPRING/"+file.getOriginalFilename())
                            .build())
                    );
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public FeedDTO.writeFeedResponse writeFeed(FeedDTO.writeFeed request) {
        return new FeedDTO.writeFeedResponse("feed writing success",
                feedRepository.save(
                    Feed.builder()
                        .contents(request.getContent())
                        .pin(request.isPin())
                        .club_id(clubRepository.findById(1).orElseThrow(RuntimeException::new)) // 차후에 수정 필요
                        .build()
                ).getId());

        }

    @Override
    public List<FeedDTO.getFeed> getFeedList(int page) {
        return feedToRepose(feedRepository.findByIdLessThanOrderByIdDesc(page));
    }

    public List<FeedDTO.getFeed> feedToRepose(List<Feed> feeds){
        List<FeedDTO.getFeed> response = new ArrayList<>();
        for(int i=0;i<feeds.size();i++){
            Feed feed = feeds.get(i);
            response.add(
                    FeedDTO.getFeed.builder()
                    .feedId(feed.getId())
                    .clubName(feed.getClub_id().getClub_name())
                    .profileImage(feed.getClub_id().getProfile_image())
                    .content(feed.getContents())
                    .media(getMediaPath(feed.getMedia()))
                    .uploadAt(feed.getUploadAt())
                    .build()
            );
        }
        return response;
    }

    public List<String> getMediaPath(List<FeedMedium> feedMedia){
        List<String> response = new ArrayList<>();
        for(int i=0;i<feedMedia.size();i++){
            FeedMedium medium = feedMedia.get(i);
            response.add(medium.getMedium_path());
        }
        return response;
    }
}
