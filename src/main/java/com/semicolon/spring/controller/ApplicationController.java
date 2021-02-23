package com.semicolon.spring.controller;

import com.semicolon.spring.dto.ApplicationDTO;
import com.semicolon.spring.service.application.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/application")
public class ApplicationController {

    private final ApplicationService applicationService;

    @DeleteMapping("/cancel")
    public ApplicationDTO.MessageResponse cancelApplication(@RequestParam("clubid") int club_id){
        return applicationService.cancelApplication(club_id);
    }

    @DeleteMapping("/remove")
    public ApplicationDTO.MessageResponse removeApplication(@RequestParam("clubid") int club_id, @RequestParam("userid") int user_id){
        return applicationService.removeApplication(club_id, user_id);
    }

    @DeleteMapping("/deport")
    public ApplicationDTO.MessageResponse deportApplication(@RequestParam("clubid") int club_id, @RequestParam("userid") int user_id){
        return applicationService.deportApplication(club_id, user_id);
    }

}
