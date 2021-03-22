package com.semicolon.spring.service.application;

import com.semicolon.spring.dto.HeadDTO;

import java.util.concurrent.ExecutionException;

public interface ApplicationService {
    HeadDTO.MessageResponse cancelApplication(int club_id) throws ExecutionException, InterruptedException;
    HeadDTO.MessageResponse removeApplication(int club_id, int user_id) throws ExecutionException, InterruptedException;
}
