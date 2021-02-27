package com.semicolon.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ApplicationDTO {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class MessageResponse{
        private String message;
    }

}
