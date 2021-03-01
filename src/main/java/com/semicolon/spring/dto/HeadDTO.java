package com.semicolon.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class HeadDTO {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class MessageResponse{
        private String message;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Builder
    public static class FcmRequest{
        private String token;
        private String title;
        private String message;
        private Integer club;
    }

}
