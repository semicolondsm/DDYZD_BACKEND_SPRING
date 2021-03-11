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
    public static class messageResponse{
        private String msg;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class recruitment{
        @NonNull
        private List<String> major;
        @JsonFormat(timezone = "Asia/Seoul")
        @NotEmpty
        private Date closeAt;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class modify{
        private String clubName;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class changeHead{
        private String userGcn;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class description{
        private String description;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class hongbo{
        private String hongbo;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class followers{
        private int followers;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class club{
        private int clubId;
        private String clubName;
        private String clubImage;
        private String clubDescription;
    }


}
