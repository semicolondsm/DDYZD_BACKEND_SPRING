package com.semicolon.spring.service.feed;

import com.semicolon.spring.dto.FeedDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FeedService {
    FeedDTO.MessageResponse fileUpload(List<MultipartFile> files, int feedId);
    FeedDTO.WriteFeedResponse writeFeed(FeedDTO.Feed request, int club_id);
    List<FeedDTO.GetFeed> getFeedList(int page);
    List<FeedDTO.GetFeedClub> getFeedClubList(int page, int club_id);
    FeedDTO.MessageResponse feedModify(FeedDTO.Feed request, int feedId);
    FeedDTO.MessageResponse feedFlag(int feedId);
    FeedDTO.GetFeed getFeed(int feedId);
    FeedDTO.MessageResponse deleteFeed(int feedId);
    FeedDTO.MessageResponse feedPin(int feedId);
    List<FeedDTO.GetFeed> getFeedList();
    List<FeedDTO.UserResponse> getFeedUser(int feedId);
}
