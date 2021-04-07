package com.semicolon.spring.service.club_head;

import com.semicolon.spring.dto.ClubDTO;

public interface ClubHeadService {
    ClubDTO.messageResponse deportMember(int club_id, int user_id);
    ClubDTO.messageResponse insertMember(int club_id, int user_id);
    ClubDTO.messageResponse changeHead(ClubDTO.changeHead request, int club_id);
    ClubDTO.messageResponse insertManager(int club_id, int user_id);
    ClubDTO.messageResponse deportManager(int club_id, int user_id);
}
