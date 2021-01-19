package com.semicolon.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
        private List<String> major;
        private Date closeAt;
    }
}
