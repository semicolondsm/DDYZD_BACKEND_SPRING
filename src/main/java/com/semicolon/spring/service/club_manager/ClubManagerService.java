package com.semicolon.spring.service.club_manager;

import com.semicolon.spring.dto.ClubDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.ExecutionException;

public interface ClubManagerService {
    ClubDTO.MessageResponse recruitment(ClubDTO.Recruitment request, int clubId) throws ExecutionException, InterruptedException;
    ClubDTO.MessageResponse clubProfile(MultipartFile file, int clubId);
    ClubDTO.MessageResponse clubHongbo(MultipartFile file, int clubId);
    ClubDTO.MessageResponse clubBanner(MultipartFile file, int clubId);
    ClubDTO.MessageResponse modifyClub(ClubDTO.Modify request, int clubId);
    ClubDTO.MessageResponse clubDescription(ClubDTO.Description request, int clubId);
    ClubDTO.MessageResponse deleteRecruitment(int clubId);
    ClubDTO.Hongbo getHongbo(int clubId);
    ClubDTO.MessageResponse deleteHongbo(int clubId);


}
