package com.semicolon.spring.controller;

import com.semicolon.spring.dto.HeadDTO;
import com.semicolon.spring.service.application.ApplicationService;
import com.semicolon.spring.service.fcm.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/application")
public class ApplicationController {

    private final ApplicationService applicationService;
    private final FcmService fcmService;

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

    @GetMapping("/test")
    public void test() throws ExecutionException, InterruptedException {
        fcmService.send(HeadDTO.FcmRequest.builder()
                .token("c9gCLOJY1EATrHG1a_Gfux:APA91bFdHLmqwqfniOtctK6V4tEpaKkl2UkNTf-TM_FVgges6AYkSFXBiTviyoRY1UnNrpuRseJx2tfXmJl-MZW2HSHneujd8kFGsIji63ZcWJHefjX9JDtDsmjPNmmuFcRqchRNtmLx")
                .title("semicolon;")
                .message("테스트임니다 ㅎㅎ")
                .build()
        );
    }

}
