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
                        .club_id(clubRepository.findById(1).orElseThrow(RuntimeException::new))
                        .build()
                ).getId());

        }
    }
