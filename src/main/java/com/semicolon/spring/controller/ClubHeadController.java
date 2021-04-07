package com.semicolon.spring.controller;

import com.semicolon.spring.dto.ClubDTO.*;
import com.semicolon.spring.service.club_head.ClubHeadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/club")
@RequiredArgsConstructor
public class ClubHeadController {

    private final ClubHeadService clubHeadService;

    @PutMapping("/{club_id}/head")
    public MessageResponse changeHead(@RequestBody ChangeHead request, @PathVariable("club_id") int club_id){
        return clubHeadService.changeHead(request, club_id);
    }

    @DeleteMapping("/deport")
    public MessageResponse deportMember(@RequestParam("clubid") int club_id, @RequestParam("userid") int user_id){
        return clubHeadService.deportMember(club_id, user_id);
    }

    @PostMapping("/insert")
    public MessageResponse insertMember(@RequestParam("clubid") int club_id, @RequestParam("userid") int user_id) {
        return clubHeadService.insertMember(club_id, user_id);
    }

    @PostMapping("/insert/manager")
    public MessageResponse insertManager(@RequestParam("clubid") int club_id, @RequestParam("userid") int user_id){
        return clubHeadService.insertManager(club_id, user_id);
    }

    @DeleteMapping("/deport/manager")
    public MessageResponse deportManager(@RequestParam("clubid") int club_id, @RequestParam("userid") int user_id){
        return clubHeadService.deportManager(club_id, user_id);
    }

}
