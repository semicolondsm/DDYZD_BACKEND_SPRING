package com.semicolon.spring.controller;

import com.semicolon.spring.dto.FeedDTO;
import com.semicolon.spring.service.feed.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@RestController
@RequiredArgsConstructor
public class FeedController {
    private final FeedService feedService;

    @PostMapping("/feed/{feedId}/medium")
    public FeedDTO.messageResponse fileUpload(@RequestParam("file")MultipartFile file, @PathVariable("feedId") int feedId){
        return feedService.fileUpload(file, feedId);
    }

    @PostMapping("/feed/{club_id}")
    public FeedDTO.writeFeedResponse feedUpload(@RequestBody FeedDTO.feed request, @PathVariable("club_id") int club_id){
        return feedService.writeFeed(request, club_id);
    }

    @GetMapping("feed/list")
    public List<FeedDTO.getFeed> getFeedList(@RequestParam("page") int page){
        return feedService.getFeedList(page);
    }

    @GetMapping("/feed/{club_id}/list")
    public List<FeedDTO.getFeedClub> getFeedClubList(@RequestParam("page") int page, @PathVariable("club_id") int club_id){
        return feedService.getFeedClubList(page, club_id);
    }

    @PutMapping("/feed/{feed_id}")
    public FeedDTO.messageResponse feedModify(@RequestBody FeedDTO.feed request, @PathVariable("feed_id") int feedId){
        return feedService.feedModify(request, feedId);
    }

    @PutMapping("/feed/{feed_id}/flag")
    public FeedDTO.messageResponse feedFlag(@PathVariable("feed_id") int feedId){
        return feedService.feedFlag(feedId);
    }

    @GetMapping("/feed/{feed_id}")
    public FeedDTO.getFeed getFeed(@PathVariable("feed_id") int feed_id){
        return feedService.getFeed(feed_id);
    }

    @DeleteMapping("/feed/{feed_id}")
    public FeedDTO.messageResponse deleteFeed(@PathVariable("feed_id") int feed_id){
        return feedService.deleteFeed(feed_id);
    }

}
