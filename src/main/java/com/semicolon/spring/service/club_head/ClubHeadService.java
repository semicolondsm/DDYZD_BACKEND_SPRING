package com.semicolon.spring.service.club_head;

import com.semicolon.spring.dto.ClubDTO;

public interface ClubHeadService {
    ClubDTO.MessageResponse deportMember(int club_id, int user_id);
    ClubDTO.MessageResponse insertMember(int club_id, int user_id);
    ClubDTO.MessageResponse changeHead(ClubDTO.ChangeHead request, int club_id);
    ClubDTO.MessageResponse insertManager(int club_id, int user_id);
    ClubDTO.MessageResponse deportManager(int club_id, int user_id);
}
