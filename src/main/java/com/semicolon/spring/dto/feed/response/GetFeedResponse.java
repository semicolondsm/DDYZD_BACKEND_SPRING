package com.semicolon.spring.dto.feed.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class GetFeedResponse {

    private int feedId;
    private String clubName;
    private int clubId;
    private String profileImage;
    private boolean isFollow;
    @JsonFormat(timezone = "Asia/Seoul")
    private LocalDateTime uploadAt;
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
