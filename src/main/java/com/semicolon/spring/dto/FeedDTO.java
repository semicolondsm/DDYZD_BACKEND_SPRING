package com.semicolon.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

public class FeedDTO {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class getFeed{
        private String clubName;
        private String profileImage;
        private boolean isFollow;
        private Date uploadAt;
        private String content;
        private List<String> media;
        private int flags;
    }
}
