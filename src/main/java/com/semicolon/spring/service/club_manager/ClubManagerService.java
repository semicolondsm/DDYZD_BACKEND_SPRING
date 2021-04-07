package com.semicolon.spring.service.club_manager;

import com.semicolon.spring.dto.ClubDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.ExecutionException;

public interface ClubManagerService {
    ClubDTO.MessageResponse recruitment(ClubDTO.Recruitment request, int club_id) throws ExecutionException, InterruptedException;
    ClubDTO.MessageResponse clubProfile(MultipartFile file, int club_id);
    ClubDTO.MessageResponse clubHongbo(MultipartFile file, int club_id);
    ClubDTO.MessageResponse clubBanner(MultipartFile file, int club_id);
    ClubDTO.MessageResponse modifyClub(ClubDTO.Modify request, int club_id);
    ClubDTO.MessageResponse clubDescription(ClubDTO.Description request, int club_id);
    ClubDTO.MessageResponse deleteRecruitment(int club_id);
    ClubDTO.Hongbo getHongbo(int club_id);
    ClubDTO.MessageResponse deleteHongbo(int club_id);


}
