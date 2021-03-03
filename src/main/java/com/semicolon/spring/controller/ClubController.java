package com.semicolon.spring.controller;

import com.semicolon.spring.dto.ClubDTO;
import com.semicolon.spring.service.club_head.ClubHeadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
public class ClubController {
    private final ClubHeadService clubHeadService;

    @PostMapping("/club/{club_id}/recruitment")
    public ClubDTO.messageResponse recruitment(@RequestBody ClubDTO.recruitment request, @PathVariable("club_id") int club_id) throws ExecutionException, InterruptedException {
        return clubHeadService.recruitment(request, club_id);
    }

    @PostMapping("/club/{club_id}/profile")
    public ClubDTO.messageResponse profile(@RequestParam("file") MultipartFile file, @PathVariable("club_id") int club_id){
        return clubHeadService.clubProfile(file, club_id);
    }

    @PostMapping("/club/{club_id}/hongbo")
    public ClubDTO.messageResponse hongbo(@RequestParam("file") MultipartFile file, @PathVariable("club_id") int club_id){
        return clubHeadService.clubHongbo(file, club_id);
    }

    @PostMapping("/club/{club_id}/banner")
    public ClubDTO.messageResponse banner(@RequestParam("file") MultipartFile file, @PathVariable("club_id") int club_id){
        return clubHeadService.clubBanner(file, club_id);
    }

    @PutMapping("/club/{club_id}")
    public ClubDTO.messageResponse modify(@RequestBody ClubDTO.modify request, @PathVariable("club_id") int club_id){
        return clubHeadService.modifyClub(request, club_id);
    }

    @PutMapping("/club/{club_id}/head")
    public ClubDTO.messageResponse changeHead(@RequestBody ClubDTO.changeHead request, @PathVariable("club_id") int club_id){
        return clubHeadService.changeHead(request, club_id);
    }

    @PostMapping("/club/{club_id}/description")
    public ClubDTO.messageResponse description(@RequestBody ClubDTO.description request, @PathVariable("club_id") int club_id){
        return clubHeadService.clubDescription(request, club_id);
    }

    @DeleteMapping("/club/{club_id}/recruitment")
    public ClubDTO.messageResponse deleteRecruitment(@PathVariable("club_id") int club_id){
        return clubHeadService.deleteRecruitment(club_id);
    }

}
