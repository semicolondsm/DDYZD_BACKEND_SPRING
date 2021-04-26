package com.semicolon.spring.controller;

import com.semicolon.spring.dto.ClubDTO;
import com.semicolon.spring.dto.ClubDTO.*;
import com.semicolon.spring.service.club_manager.ClubManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/club")
@RequiredArgsConstructor
public class ClubManagerController {

    private final ClubManagerService clubHeadService;

    @PostMapping("/{club_id}/recruitment")
    public MessageResponse recruitment(@RequestBody @Valid ClubDTO.Recruitment request, @PathVariable("club_id") int clubId) throws ExecutionException, InterruptedException {
        return clubHeadService.recruitment(request, clubId);
    }

    @PostMapping("/{club_id}/profile")
    public MessageResponse profile(@RequestParam("file") MultipartFile file, @PathVariable("club_id") int clubId){
        return clubHeadService.clubProfile(file, clubId);
    }

    @PostMapping("/{club_id}/hongbo")
    public MessageResponse hongbo(@RequestParam("file") MultipartFile file, @PathVariable("club_id") int clubId){
        return clubHeadService.clubHongbo(file, clubId);
    }

    @PostMapping("/{club_id}/banner")
    public MessageResponse banner(@RequestParam("file") MultipartFile file, @PathVariable("club_id") int clubId){
        return clubHeadService.clubBanner(file, clubId);
    }

    @PutMapping("/{club_id}")
    public MessageResponse modify(@RequestBody Modify request, @PathVariable("club_id") int clubId){
        return clubHeadService.modifyClub(request, clubId);
    }

    @PostMapping("/{club_id}/description")
    public MessageResponse description(@RequestBody Description request, @PathVariable("club_id") int clubId){
        return clubHeadService.clubDescription(request, clubId);
    }

    @DeleteMapping("/{club_id}/recruitment")
    public MessageResponse deleteRecruitment(@PathVariable("club_id") int clubId){
        return clubHeadService.deleteRecruitment(clubId);
    }

    @GetMapping("/{club_id}/hongbo")
    public Hongbo getHongbo(@PathVariable("club_id") int clubId){
        return clubHeadService.getHongbo(clubId);
    }

    @DeleteMapping("/{club_id}/hongbo")
    public MessageResponse deleteHongbo(@PathVariable("club_id") int clubId){
        return clubHeadService.deleteHongbo(clubId);
    }



}
