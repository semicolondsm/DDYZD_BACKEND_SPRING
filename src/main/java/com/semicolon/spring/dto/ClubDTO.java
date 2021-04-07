package com.semicolon.spring.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

public class ClubDTO {
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class MessageResponse {
        private String msg;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Recruitment {
        @NonNull
        private List<String> major;
        @JsonFormat(timezone = "Asia/Seoul")
        @NonNull
        private Date closeAt;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Modify {
        private String clubName;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class ChangeHead {
        private String userGcn;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Description {
        private String description;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Hongbo {
        private String hongbo;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Followers {
        private int followers;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class Club {
        private int clubId;
        private String clubName;
        private String clubImage;
        private String clubDescription;
    }


}
