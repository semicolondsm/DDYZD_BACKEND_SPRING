package com.semicolon.spring.controller;

import com.semicolon.spring.dto.FeedDTO;
import com.semicolon.spring.service.feed.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class FeedController {
    private final FeedService feedService;

    @PostMapping("/test")
    public void fileUpload(@RequestParam("file")MultipartFile file){
        feedService.fileUpload(file);
    }

    @PostMapping("/feed/test")
    public FeedDTO.writeFeedResponse feedUpload(@RequestBody FeedDTO.writeFeed request){
        return feedService.writeFeed(request);
    }
}
