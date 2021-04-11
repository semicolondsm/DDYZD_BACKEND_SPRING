package com.semicolon.spring.controller;

import com.semicolon.spring.dto.HeadDTO.*;
import com.semicolon.spring.service.application.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/application")
public class ApplicationController {
    
    private final ApplicationService applicationService;

    @DeleteMapping("/cancel")
    public MessageResponse cancelApplication(@RequestParam("clubid") int clubId) throws ExecutionException, InterruptedException {
        return applicationService.cancelApplication(clubId);
    }

    @DeleteMapping("/remove")
    public MessageResponse removeApplication(@RequestParam("clubid") int clubId, @RequestParam("userid") int userId) throws ExecutionException, InterruptedException {
        return applicationService.removeApplication(clubId, userId);
    }



}
