package com.semicolon.spring.service.club;


import com.semicolon.spring.dto.club.response.ClubResponse;
import com.semicolon.spring.dto.club.response.FollowersResponse;

import java.util.List;


public interface ClubService {
    FollowersResponse getFollowers(int clubId);
    List<ClubResponse> getClubs();
}
