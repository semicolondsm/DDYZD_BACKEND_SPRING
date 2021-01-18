package com.semicolon.spring.service.feed;

import com.semicolon.spring.dto.FeedDTO;
import com.semicolon.spring.entity.club.Club;
import com.semicolon.spring.entity.club.ClubRepository;
import com.semicolon.spring.entity.feed.Feed;
import com.semicolon.spring.entity.feed.FeedRepository;
import com.semicolon.spring.entity.feed_medium.FeedMedium;
import com.semicolon.spring.entity.feed_medium.FeedMediumRepository;
import com.semicolon.spring.exception.ClubNotExistException;
import lombok.RequiredArgsConstructor;
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
                        .clubId(clubRepository.findById(1).orElseThrow(ClubNotExistException::new)) // 차후에 수정 필요
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
