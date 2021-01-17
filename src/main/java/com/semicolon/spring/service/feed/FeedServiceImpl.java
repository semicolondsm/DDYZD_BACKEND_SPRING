package com.semicolon.spring.service.feed;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FeedServiceImpl implements FeedService{
    @Override
    public void fileUpload(MultipartFile file) {
        try{
            file.transferTo(new File("C:/Semicolon/DDYZD_V2_BACKEND_SPRING/"+file.getOriginalFilename()));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
