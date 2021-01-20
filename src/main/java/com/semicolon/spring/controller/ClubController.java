package com.semicolon.spring.controller;

import com.semicolon.spring.dto.ClubDTO;
import com.semicolon.spring.service.club_head.ClubHeadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ClubController {
    private final ClubHeadService clubHeadService;

    @PostMapping("/club/{club_id}/recruitment")
    public ClubDTO.messageResponse recruitment(@RequestBody ClubDTO.recruitment request, @PathVariable("club_id") int club_id){
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
}
