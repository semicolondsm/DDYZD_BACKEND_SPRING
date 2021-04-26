package com.semicolon.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class HeadDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageResponse{
        private String message;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FcmRequest{
        private String token;
        private String title;
        private String message;
        private Integer club;

        public void setMessage(String message){
            this.message = message;
        }
    }

}
