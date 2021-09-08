package com.semicolon.spring.dto.feed.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class GetFeedClubResponse extends GetFeedResponse {

    private boolean isPin;

    @Builder(builderMethodName = "getFeedClubResponseBuilder")
    public GetFeedClubResponse(int feedId, String clubName, int clubId,
                               String profileImage, boolean isFollow,
                               LocalDateTime uploadAt, String content, List<String> media,
                               boolean isFlag, int flags, boolean isOwner, boolean isPin) {
        super(feedId, clubName, clubId,
                profileImage, isFollow,
                uploadAt, content, media,
                isFlag, flags, isOwner);
        this.isPin = isPin;
    }
}
