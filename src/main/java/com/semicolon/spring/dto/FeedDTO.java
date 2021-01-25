package com.semicolon.spring.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
        @JsonFormat(timezone = "Asia/Seoul")
        private Date uploadAt;
        private String content;
        private List<String> media;
        private boolean isFlag;
        private int flags;

        public void setIsFollow(boolean value){
            this.isFollow = value;
        }

        public void setIsFlag(boolean value){
            this.isFlag = value;
        }
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
        @JsonFormat(timezone = "Asia/Seoul")
        private Date uploadAt;
        private String content;
        private List<String> media;
        private boolean isFlag;
        private int flags;
        private boolean isPin;

        public void setIsFollow(boolean value){
            this.isFollow = value;
        }

        public void setIsFlag(boolean value){
            this.isFlag = value;
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class feed{
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

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class messageResponse{
        private String msg;
    }
}
