package com.semicolon.spring.controller;

import com.semicolon.spring.dto.ClubDTO;
import com.semicolon.spring.service.club_head.ClubHeadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/club")
@RequiredArgsConstructor
public class ClubHeadController {

    private final ClubHeadService clubHeadService;

    @PutMapping("/{club_id}/head")
    public ClubDTO.messageResponse changeHead(@RequestBody ClubDTO.changeHead request, @PathVariable("club_id") int club_id){
        return clubHeadService.changeHead(request, club_id);
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
