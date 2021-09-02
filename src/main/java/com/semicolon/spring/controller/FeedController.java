package com.semicolon.spring.controller;

import com.semicolon.spring.dto.feed.request.ContentRequest;
import com.semicolon.spring.dto.feed.response.GetFeedClubResponse;
import com.semicolon.spring.dto.feed.response.GetFeedResponse;
import com.semicolon.spring.dto.user.response.UserInfoResponse;
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
    public void fileUpload(@RequestParam("files") List<MultipartFile> files, @PathVariable("feedId") int feedId){
        feedService.fileUpload(files, feedId);
    }

    @PostMapping("/feed/{club_id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void feedUpload(@RequestBody ContentRequest request, @PathVariable("club_id") int clubId){
        feedService.writeFeed(request, clubId);
    }

    @GetMapping("/feed/list")
    public List<GetFeedResponse> getFeedList(@RequestParam("page") int page){
        if(page < 0){
            throw new BadRequestException();
        }
        return feedService.getFeedList(page);
    }

    @GetMapping("/feed/{club_id}/list")
    public List<GetFeedClubResponse> getFeedClubList(@RequestParam("page") int page, @PathVariable("club_id") int clubId){
        if(page < 0){
            throw new BadRequestException();
        }
        return feedService.getFeedClubList(page, clubId);
    }

    @PutMapping("/feed/{feed_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void feedModify(@RequestBody ContentRequest request, @PathVariable("feed_id") int feedId){
        feedService.feedModify(request, feedId);
    }

    @PutMapping("/feed/{feed_id}/flag")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void feedFlag(@PathVariable("feed_id") int feedId){
        feedService.feedFlag(feedId);
    }

    @GetMapping("/feed/{feed_id}")
    public GetFeedResponse getFeed(@PathVariable("feed_id") int feedId){
        return feedService.getFeed(feedId);
    }

    @DeleteMapping("/feed/{feed_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFeed(@PathVariable("feed_id") int feedId){
        feedService.deleteFeed(feedId);
    }

    @PutMapping("/feed/{feed_id}/pin")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void feedPin(@PathVariable("feed_id") int feedId){
        feedService.feedPin(feedId);
    }

    @GetMapping("/feed/flag")
    public List<GetFeedResponse> getFlagFeed(@RequestParam("page") int page){
        if(page < 0){
            throw new BadRequestException();
        }
        return feedService.getFlagList(page);
    }

    @GetMapping("/feed/{feed_id}/user")
    public List<UserInfoResponse> getFlagUser(@PathVariable("feed_id") int feedId){
        return feedService.getFeedUser(feedId);
    }

}
