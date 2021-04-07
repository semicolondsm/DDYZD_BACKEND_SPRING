package com.semicolon.spring.controller;

import com.semicolon.spring.dto.ClubDTO;
import com.semicolon.spring.service.club.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/club")
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;

    @GetMapping("/{club_id}/follow")
    public ClubDTO.followers getFollowers(@PathVariable("club_id") int club_id){
        return clubService.getFollowers(club_id);
    }

    @GetMapping("/follow")
    public List<ClubDTO.club> getClubs(){
        return clubService.getClubs();
    }
}
