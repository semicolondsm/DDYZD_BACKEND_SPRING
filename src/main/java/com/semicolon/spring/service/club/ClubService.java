package com.semicolon.spring.service.club;

import com.semicolon.spring.dto.ClubDTO;

import java.util.List;

public interface ClubService {
    ClubDTO.followers getFollowers(int club_id);
    List<ClubDTO.club> getClubs();
}
