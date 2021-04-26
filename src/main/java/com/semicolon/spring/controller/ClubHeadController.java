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
    public MessageResponse changeHead(@RequestBody ChangeHead request, @PathVariable("club_id") int clubId){
        return clubHeadService.changeHead(request, clubId);
    }

    @DeleteMapping("/deport")
    public MessageResponse deportMember(@RequestParam("clubid") int clubId, @RequestParam("userid") int userId){
        return clubHeadService.deportMember(clubId, userId);
    }

    @PostMapping("/insert")
    public MessageResponse insertMember(@RequestParam("clubid") int clubId, @RequestParam("userid") int userId) {
        return clubHeadService.insertMember(clubId, userId);
    }

    @PostMapping("/insert/manager")
    public MessageResponse insertManager(@RequestParam("clubid") int clubId, @RequestParam("userid") int userId){
        return clubHeadService.insertManager(clubId, userId);
    }

    @DeleteMapping("/deport/manager")
    public MessageResponse deportManager(@RequestParam("clubid") int clubId, @RequestParam("userid") int userId){
        return clubHeadService.deportManager(clubId, userId);
    }

}
