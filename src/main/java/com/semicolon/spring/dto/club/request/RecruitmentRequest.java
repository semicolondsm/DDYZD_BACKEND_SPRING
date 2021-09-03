package com.semicolon.spring.dto.club.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor
public class RecruitmentRequest {

    @NonNull
    private List<String> major;
    @JsonFormat(timezone = "Asia/Seoul")
    @NonNull
    private Date closeAt;

}
