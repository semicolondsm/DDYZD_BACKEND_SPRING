package com.semicolon.spring.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.semicolon.spring.entity.club.Club;
import com.semicolon.spring.entity.user.User;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

public class ClubDTO {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageResponse {
        private String msg;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Recruitment {
        @NonNull
        private List<String> major;
        @JsonFormat(timezone = "Asia/Seoul")
        @NonNull
        private Date closeAt;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Modify {
        private String clubName;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangeHead {
        private String userGcn;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Description {
        private String description;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Hongbo {
        private String hongbo;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Followers {
        private int followers;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Club {
        private int clubId;
        private String clubName;
        private String clubImage;
        private String clubDescription;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Information {
        private User user;
        private com.semicolon.spring.entity.club.Club club;
    }


}
