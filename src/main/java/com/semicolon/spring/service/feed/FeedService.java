package com.semicolon.spring.service.feed;

import com.semicolon.spring.dto.FeedDTO;
import com.semicolon.spring.entity.feed.Feed;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FeedService {
    void fileUpload(MultipartFile file, int feedId);
    FeedDTO.writeFeedResponse writeFeed(FeedDTO.writeFeed request);
    List<FeedDTO.getFeed> getFeedList(int page);
}
