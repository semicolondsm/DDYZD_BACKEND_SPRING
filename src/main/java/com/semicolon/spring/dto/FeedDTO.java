package com.semicolon.spring.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

public class FeedDTO {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class getFeed{
        private int feedId;
        private String clubName;
        private String profileImage;
        private boolean isFollow;
        private Date uploadAt;
        private String content;
        private List<String> media;
        private int flags;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class getFeedClub{
        private int feedId;
        private String clubName;
        private String profileImage;
        private boolean isFollow;
        private Date uploadAt;
        private String content;
        private List<String> media;
        private int flags;
        private boolean isPin;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class writeFeed{
        private String content;
        @JsonProperty
        private boolean isPin;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class writeFeedResponse{
        private String msg;
        private int feedId;
    }
}
