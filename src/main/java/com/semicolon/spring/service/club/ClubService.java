package com.semicolon.spring.service.club;

import com.semicolon.spring.dto.ClubDTO;

import java.util.List;

public interface ClubService {
    ClubDTO.Followers getFollowers(int clubId);
    List<ClubDTO.Club> getClubs();
}
