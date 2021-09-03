package com.semicolon.spring.controller;

import com.semicolon.spring.dto.club.response.ClubResponse;
import com.semicolon.spring.dto.club.response.FollowersResponse;
import com.semicolon.spring.service.club.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/club")
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;

    @GetMapping("/{club_id}/follow")
    public FollowersResponse getFollowers(@PathVariable("club_id") int clubId){
        return clubService.getFollowers(clubId);
    }

    @GetMapping("/follow")
    public List<ClubResponse> getClubs(){
        return clubService.getClubs();
    }
}
