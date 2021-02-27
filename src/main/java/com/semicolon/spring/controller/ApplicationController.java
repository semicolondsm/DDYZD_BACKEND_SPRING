package com.semicolon.spring.controller;

import com.semicolon.spring.dto.HeadDTO;
import com.semicolon.spring.service.application.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/application")
public class ApplicationController {

    private final ApplicationService applicationService;

    @DeleteMapping("/cancel")
    public HeadDTO.MessageResponse cancelApplication(@RequestParam("clubid") int club_id) throws ExecutionException, InterruptedException {
        return applicationService.cancelApplication(club_id);
    }

    @DeleteMapping("/remove")
    public HeadDTO.MessageResponse removeApplication(@RequestParam("clubid") int club_id, @RequestParam("userid") int user_id) throws ExecutionException, InterruptedException {
        return applicationService.removeApplication(club_id, user_id);
    }

    @DeleteMapping("/deport")
    public HeadDTO.MessageResponse deportApplication(@RequestParam("clubid") int club_id, @RequestParam("userid") int user_id){
        return applicationService.deportApplication(club_id, user_id);
    }

}
