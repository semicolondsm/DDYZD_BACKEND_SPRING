package com.semicolon.spring.service.feed;

import com.semicolon.spring.dto.feed.request.ContentRequest;
import com.semicolon.spring.dto.feed.response.GetFeedClubResponse;
import com.semicolon.spring.dto.feed.response.GetFeedResponse;
import com.semicolon.spring.dto.user.response.UserInfoResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FeedService {
    void fileUpload(List<MultipartFile> files, int feedId);
    void writeFeed(ContentRequest request, int clubId);
    List<GetFeedResponse> getFeedList(int page);
    List<GetFeedClubResponse> getFeedClubList(int page, int clubId);
    void feedModify(ContentRequest request, int feedId);
    void feedFlag(int feedId);
    GetFeedResponse getFeed(int feedId);
    void deleteFeed(int feedId);
    void feedPin(int feedId);
    List<GetFeedResponse> getFlagList(int page);
    List<UserInfoResponse> getFeedUser(int feedId);
}
