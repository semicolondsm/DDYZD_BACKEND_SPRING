package com.semicolon.spring.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

public class FeedDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetFeed {
        private int feedId;
        private String clubName;
        private int clubId;
        private String profileImage;
        private boolean isFollow;
        @JsonFormat(timezone = "Asia/Seoul")
        private Date uploadAt;
        private String content;
        private List<String> media;
        private boolean isFlag;
        private int flags;
        private boolean isOwner;

        public void setIsFollow(boolean value){
            this.isFollow = value;
        }

        public void setIsFlag(boolean value){
            this.isFlag = value;
        }

        public void setOwner(boolean value){
            this.isOwner = value;
        }

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetFeedClub {
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
        private boolean isOwner;

        public void setIsFollow(boolean value){
            this.isFollow = value;
        }

        public void setIsFlag(boolean value){
            this.isFlag = value;
        }

        public void setOwner(boolean value){
            this.isOwner = value;
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Feed {
        private String content;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WriteFeedResponse {
        private String msg;
        private int feedId;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageResponse {
        private String msg;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserResponse {
        private int userId;
        private String userName;
        private String imagePath;
    }

}
