package com.semicolon.spring.service.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import com.semicolon.spring.dto.HeadDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class FcmService {

    private static final String FIREBASE_CONFIG_PATH = "ddyzd-firebase-adminsdk.json";

    @PostConstruct
    public void initialize(){
        try{
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(FIREBASE_CONFIG_PATH).getInputStream())).build();
            if(FirebaseApp.getApps().isEmpty()){
                FirebaseApp.initializeApp(options);
                log.info("Firebase application has been initialized");
            }
        }catch (IOException e){
            log.error(e.getMessage());
        }
    }

    public void send(HeadDTO.FcmRequest request) throws ExecutionException, InterruptedException {
        Message message = Message.builder()
                .setToken(request.getToken())
                .setWebpushConfig(WebpushConfig.builder().putHeader("ttl", "300")
                        .setNotification(new WebpushNotification(request.getTitle(),
                                request.getMessage()))
                        .build()
                ).build();

        String response = FirebaseMessaging.getInstance().sendAsync(message).get();
        log.info("Sent Message");
    }

}
