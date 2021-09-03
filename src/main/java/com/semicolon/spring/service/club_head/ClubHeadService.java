package com.semicolon.spring.service.club_head;

import com.semicolon.spring.dto.club.request.ChangeHeadRequest;

public interface ClubHeadService {
    void deportMember(int clubId, int userId);
    void insertMember(int clubId, int userId);
    void changeHead(ChangeHeadRequest request, int clubId);
    void insertManager(int clubId, int userId);
    void deportManager(int clubId, int userId);
}
