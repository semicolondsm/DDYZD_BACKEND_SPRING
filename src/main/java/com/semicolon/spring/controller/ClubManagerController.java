package com.semicolon.spring.controller;

import com.semicolon.spring.dto.club.request.ClubNameModifyRequest;
import com.semicolon.spring.dto.club.request.DescriptionRequest;
import com.semicolon.spring.dto.club.request.RecruitmentRequest;
import com.semicolon.spring.dto.club.response.HongboResponse;
import com.semicolon.spring.service.club_manager.ClubManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public void recruitment(@RequestBody @Valid RecruitmentRequest request, @PathVariable("club_id") int clubId) throws ExecutionException, InterruptedException {
        clubHeadService.recruitment(request, clubId);
    }

    @PostMapping("/{club_id}/profile")
    @ResponseStatus(HttpStatus.CREATED)
    public void profile(@RequestParam("file") MultipartFile file, @PathVariable("club_id") int clubId){
        clubHeadService.clubProfile(file, clubId);
    }

    @PostMapping("/{club_id}/hongbo")
    @ResponseStatus(HttpStatus.CREATED)
    public void hongbo(@RequestParam("file") MultipartFile file, @PathVariable("club_id") int clubId){
        clubHeadService.clubHongbo(file, clubId);
    }

    @PostMapping("/{club_id}/banner")
    @ResponseStatus(HttpStatus.CREATED)
    public void banner(@RequestParam("file") MultipartFile file, @PathVariable("club_id") int clubId){
        clubHeadService.clubBanner(file, clubId);
    }

    @PutMapping("/{club_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void modify(@RequestBody ClubNameModifyRequest request, @PathVariable("club_id") int clubId){
        clubHeadService.modifyClub(request, clubId);
    }

    @PostMapping("/{club_id}/description")
    @ResponseStatus(HttpStatus.CREATED)
    public void description(@RequestBody DescriptionRequest request, @PathVariable("club_id") int clubId){
        clubHeadService.clubDescription(request, clubId);
    }

    @DeleteMapping("/{club_id}/recruitment")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecruitment(@PathVariable("club_id") int clubId){
        clubHeadService.deleteRecruitment(clubId);
    }

    @GetMapping("/{club_id}/hongbo")
    public HongboResponse getHongbo(@PathVariable("club_id") int clubId){
        return clubHeadService.getHongbo(clubId);
    }

    @DeleteMapping("/{club_id}/hongbo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteHongbo(@PathVariable("club_id") int clubId){
        clubHeadService.deleteHongbo(clubId);
    }



}
