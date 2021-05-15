package com.semicolon.spring.controller;

import com.semicolon.spring.dto.FeedDTO.*;
import com.semicolon.spring.exception.BadRequestException;
import com.semicolon.spring.service.feed.FeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FeedController {
    private final FeedService feedService;

    @PostMapping("/feed/{feedId}/medium")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponse fileUpload(@RequestParam("files") List<MultipartFile> files, @PathVariable("feedId") int feedId){
        return feedService.fileUpload(files, feedId);
    }

    @PostMapping("/feed/{club_id}")
    @ResponseStatus(HttpStatus.CREATED)
    public WriteFeedResponse feedUpload(@RequestBody Feed request, @PathVariable("club_id") int clubId){
        return feedService.writeFeed(request, clubId);
    }

    @GetMapping("/feed/list")
    public List<GetFeed> getFeedList(@RequestParam("page") int page){
        if(page < 0){
            throw new BadRequestException();
        }
        return feedService.getFeedList(page);
    }

    @GetMapping("/feed/{club_id}/list")
    public List<GetFeedClub> getFeedClubList(@RequestParam("page") int page, @PathVariable("club_id") int clubId){
        if(page < 0){
            throw new BadRequestException();
        }
        return feedService.getFeedClubList(page, clubId);
    }

    @PutMapping("/feed/{feed_id}")
    public MessageResponse feedModify(@RequestBody Feed request, @PathVariable("feed_id") int feedId){
        return feedService.feedModify(request, feedId);
    }

    @PutMapping("/feed/{feed_id}/flag")
    public MessageResponse feedFlag(@PathVariable("feed_id") int feedId){
        return feedService.feedFlag(feedId);
    }

    @GetMapping("/feed/{feed_id}")
    public GetFeed getFeed(@PathVariable("feed_id") int feedId){
        return feedService.getFeed(feedId);
    }

    @DeleteMapping("/feed/{feed_id}")
    public MessageResponse deleteFeed(@PathVariable("feed_id") int feedId){
        return feedService.deleteFeed(feedId);
    }

    @PutMapping("/feed/{feed_id}/pin")
    public MessageResponse feedPin(@PathVariable("feed_id") int feedId){
        return feedService.feedPin(feedId);
    }

    @GetMapping("/feed/flag")
    public List<GetFeed> getFlagFeed(@RequestParam("page") int page){
        if(page < 0){
            throw new BadRequestException();
        }
        return feedService.getFlagList(page);
    }

    @GetMapping("/feed/{feed_id}/user")
    public List<UserResponse> getFlagUser(@PathVariable("feed_id") int feedId){
        return feedService.getFeedUser(feedId);
    }

}
