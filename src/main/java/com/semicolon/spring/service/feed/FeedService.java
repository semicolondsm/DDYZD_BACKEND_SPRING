package com.semicolon.spring.service.feed;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FeedService {
    void fileUpload(MultipartFile file);
}
