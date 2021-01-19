package com.semicolon.spring.service.club_head;

import com.semicolon.spring.dto.ClubDTO;

public interface ClubHeadService {
    ClubDTO.messageResponse recruitment(ClubDTO.recruitment request, int club_id);
}
