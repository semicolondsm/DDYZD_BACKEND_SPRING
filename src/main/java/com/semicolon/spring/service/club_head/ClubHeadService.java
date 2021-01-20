package com.semicolon.spring.service.club_head;

import com.semicolon.spring.dto.ClubDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ClubHeadService {
    ClubDTO.messageResponse recruitment(ClubDTO.recruitment request, int club_id);
    ClubDTO.messageResponse clubProfile(MultipartFile file, int club_id);

}
