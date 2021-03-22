package com.semicolon.spring.controller;

import com.semicolon.spring.dto.ClubDTO;
import com.semicolon.spring.dto.HeadDTO;
import com.semicolon.spring.service.club.ClubService;
import com.semicolon.spring.service.club_head.ClubHeadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController("/club")
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;
    private final ClubHeadService clubHeadService;

    @PostMapping("/{club_id}/recruitment")
    public ClubDTO.messageResponse recruitment(@RequestBody @Valid ClubDTO.recruitment request, @PathVariable("club_id") int club_id) throws ExecutionException, InterruptedException {
        return clubHeadService.recruitment(request, club_id);
    }

    @PostMapping("/{club_id}/profile")
    public ClubDTO.messageResponse profile(@RequestParam("file") MultipartFile file, @PathVariable("club_id") int club_id){
        return clubHeadService.clubProfile(file, club_id);
    }

    @PostMapping("/{club_id}/hongbo")
    public ClubDTO.messageResponse hongbo(@RequestParam("file") MultipartFile file, @PathVariable("club_id") int club_id){
        return clubHeadService.clubHongbo(file, club_id);
    }

    @PostMapping("/{club_id}/banner")
    public ClubDTO.messageResponse banner(@RequestParam("file") MultipartFile file, @PathVariable("club_id") int club_id){
        return clubHeadService.clubBanner(file, club_id);
    }

    @PutMapping("/{club_id}")
    public ClubDTO.messageResponse modify(@RequestBody ClubDTO.modify request, @PathVariable("club_id") int club_id){
        return clubHeadService.modifyClub(request, club_id);
    }

    @PutMapping("/{club_id}/head")
    public ClubDTO.messageResponse changeHead(@RequestBody ClubDTO.changeHead request, @PathVariable("club_id") int club_id){
        return clubHeadService.changeHead(request, club_id);
    }

    @PostMapping("/{club_id}/description")
    public ClubDTO.messageResponse description(@RequestBody ClubDTO.description request, @PathVariable("club_id") int club_id){
        return clubHeadService.clubDescription(request, club_id);
    }

    @DeleteMapping("/{club_id}/recruitment")
    public ClubDTO.messageResponse deleteRecruitment(@PathVariable("club_id") int club_id){
        return clubHeadService.deleteRecruitment(club_id);
    }

    @GetMapping("/{club_id}/hongbo")
    public ClubDTO.hongbo getHongbo(@PathVariable("club_id") int club_id){
        return clubHeadService.getHongbo(club_id);
    }

    @DeleteMapping("/{club_id}/hongbo")
    public ClubDTO.messageResponse deleteHongbo(@PathVariable("club_id") int club_id){
        return clubHeadService.deleteHongbo(club_id);
    }

    @GetMapping("/{club_id}/follow")
    public ClubDTO.followers getFollowers(@PathVariable("club_id") int club_id){
        return clubService.getFollowers(club_id);
    }

    @GetMapping("/follow")
    public List<ClubDTO.club> getClubs(){
        return clubService.getClubs();
    }

    @DeleteMapping("/deport")
    public ClubDTO.messageResponse deportApplication(@RequestParam("clubid") int club_id, @RequestParam("userid") int user_id){
        return clubHeadService.deportMember(club_id, user_id);
    }

    @PostMapping("/insert")
    public ClubDTO.messageResponse insertMember(@RequestParam("clubid") int club_id, @RequestParam("userid") int user_id) {
        return clubHeadService.insertMember(club_id, user_id);
    }

}
