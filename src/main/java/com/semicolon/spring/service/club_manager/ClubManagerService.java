package com.semicolon.spring.service.club_manager;

import com.semicolon.spring.dto.ClubDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.ExecutionException;

public interface ClubManagerService {
    ClubDTO.messageResponse recruitment(ClubDTO.recruitment request, int club_id) throws ExecutionException, InterruptedException;
    ClubDTO.messageResponse clubProfile(MultipartFile file, int club_id);
    ClubDTO.messageResponse clubHongbo(MultipartFile file, int club_id);
    ClubDTO.messageResponse clubBanner(MultipartFile file, int club_id);
    ClubDTO.messageResponse modifyClub(ClubDTO.modify request, int club_id);
    ClubDTO.messageResponse clubDescription(ClubDTO.description request, int club_id);
    ClubDTO.messageResponse deleteRecruitment(int club_id);
    ClubDTO.hongbo getHongbo(int club_id);
    ClubDTO.messageResponse deleteHongbo(int club_id);


}
