package com.semicolon.spring.service.feed;

import com.semicolon.spring.dto.FeedDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FeedService {
    void fileUpload(MultipartFile file);
    FeedDTO.writeFeedResponse writeFeed(FeedDTO.writeFeed request);
}
