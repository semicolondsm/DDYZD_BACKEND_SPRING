package com.semicolon.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class ErpDTO {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Url{
        private String url;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Supply{
        private String name;
        private List<String> options;
        private List<String> image_url;
    }

}
