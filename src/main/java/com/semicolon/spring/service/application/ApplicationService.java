package com.semicolon.spring.service.application;

import com.semicolon.spring.Application;
import com.semicolon.spring.dto.ApplicationDTO;

public interface ApplicationService {
    ApplicationDTO.MessageResponse cancelApplication(int club_id);
    ApplicationDTO.MessageResponse removeApplication(int club_id, int user_id);
    ApplicationDTO.MessageResponse deportApplication(int club_id, int user_id);
}
