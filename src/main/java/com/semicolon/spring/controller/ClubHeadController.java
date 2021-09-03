package com.semicolon.spring.controller;

import com.semicolon.spring.dto.club.request.ChangeHeadRequest;
import com.semicolon.spring.service.club_head.ClubHeadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/club")
@RequiredArgsConstructor
public class ClubHeadController {

    private final ClubHeadService clubHeadService;

    @PutMapping("/{club_id}/head")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeHead(@RequestBody ChangeHeadRequest request, @PathVariable("club_id") int clubId){
        clubHeadService.changeHead(request, clubId);
    }

    @DeleteMapping("/deport")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deportMember(@RequestParam("clubid") int clubId, @RequestParam("userid") int userId){
        clubHeadService.deportMember(clubId, userId);
    }

    @PostMapping("/insert")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void insertMember(@RequestParam("clubid") int clubId, @RequestParam("userid") int userId) {
        clubHeadService.insertMember(clubId, userId);
    }

    @PostMapping("/insert/manager")
    @ResponseStatus(HttpStatus.CREATED)
    public void insertManager(@RequestParam("clubid") int clubId, @RequestParam("userid") int userId){
        clubHeadService.insertManager(clubId, userId);
    }

    @DeleteMapping("/deport/manager")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deportManager(@RequestParam("clubid") int clubId, @RequestParam("userid") int userId){
        clubHeadService.deportManager(clubId, userId);
    }

}
