package com.semicolon.spring.service.club_manager;

import com.semicolon.spring.dto.club.request.ClubNameModifyRequest;
import com.semicolon.spring.dto.club.request.DescriptionRequest;
import com.semicolon.spring.dto.club.request.RecruitmentRequest;
import com.semicolon.spring.dto.club.response.HongboResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.ExecutionException;

public interface ClubManagerService {
    void recruitment(RecruitmentRequest request, int clubId) throws ExecutionException, InterruptedException;
    void clubProfile(MultipartFile file, int clubId);
    void clubHongbo(MultipartFile file, int clubId);
    void clubBanner(MultipartFile file, int clubId);
    void modifyClub(ClubNameModifyRequest request, int clubId);
    void clubDescription(DescriptionRequest request, int clubId);
    void deleteRecruitment(int clubId);
    HongboResponse getHongbo(int clubId);
    void deleteHongbo(int clubId);


}
