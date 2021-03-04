package com.semicolon.spring.service.club;

import com.semicolon.spring.dto.ClubDTO;

public interface ClubService {
    ClubDTO.followers getFollowers(int club_id);
}
