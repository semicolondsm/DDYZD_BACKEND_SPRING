package com.semicolon.spring.dto.club.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClubResponse {

    private int clubId;
    private String clubName;
    private String clubImage;
    private String clubDescription;

}
