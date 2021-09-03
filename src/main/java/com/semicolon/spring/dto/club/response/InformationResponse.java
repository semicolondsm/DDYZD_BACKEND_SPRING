package com.semicolon.spring.dto.club.response;

import com.semicolon.spring.entity.club.Club;
import com.semicolon.spring.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InformationResponse {

    private User user;
    private Club club;

}
