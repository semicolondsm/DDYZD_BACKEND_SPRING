package com.semicolon.spring.service.club_head;

import com.semicolon.spring.dto.ClubDTO;

public interface ClubHeadService {
    ClubDTO.MessageResponse deportMember(int clubId, int userId);
    ClubDTO.MessageResponse insertMember(int clubId, int userId);
    ClubDTO.MessageResponse changeHead(ClubDTO.ChangeHead request, int clubId);
    ClubDTO.MessageResponse insertManager(int clubId, int userId);
    ClubDTO.MessageResponse deportManager(int clubId, int userId);
}
