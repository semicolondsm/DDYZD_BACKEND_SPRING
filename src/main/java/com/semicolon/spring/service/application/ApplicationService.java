package com.semicolon.spring.service.application;

import com.semicolon.spring.dto.HeadDTO;

import java.util.concurrent.ExecutionException;

public interface ApplicationService {
    HeadDTO.MessageResponse cancelApplication(int clubId) throws ExecutionException, InterruptedException;
    HeadDTO.MessageResponse removeApplication(int clubId, int userId) throws ExecutionException, InterruptedException;
}
