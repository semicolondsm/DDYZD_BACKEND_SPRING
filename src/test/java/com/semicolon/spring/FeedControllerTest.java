package com.semicolon.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.semicolon.spring.dto.feed.request.ContentRequest;
import com.semicolon.spring.entity.club.Club;
import com.semicolon.spring.entity.club.ClubRepository;
import com.semicolon.spring.entity.club.club_head.ClubHead;
import com.semicolon.spring.entity.club.club_head.ClubHeadRepository;
import com.semicolon.spring.entity.club.club_member.ClubMember;
import com.semicolon.spring.entity.club.club_member.ClubMemberRepository;
import com.semicolon.spring.entity.feed.FeedRepository;
import com.semicolon.spring.entity.user.User;
import com.semicolon.spring.entity.user.UserRepository;
import com.semicolon.spring.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"spring.config.location=classpath:application-test.properties"})
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FeedControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private ClubHeadRepository clubHeadRepository;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private ClubMemberRepository clubMemberRepository;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private MockMvc mvc;

    String accessToken;

    @BeforeEach
    void setup(){
        //mvc = webAppContextSetup(context).build();

        User user = userRepository.save(
                User.builder()
                .imagePath("test")
                .name("이서준")
                .gcn("2114")
                .build()
        );

        accessToken = jwtTokenProvider.generateAccessToken(1);
        Club club = clubRepository.save(
                Club.builder()
                .name("SEMICOLON;")
                .totalBudget(0)
                .currentBudget(0)
                .bannerImage("test")
                .profileImage("test")
                .hongboImage("test")
                .build()
        );

        clubMemberRepository.save(
                ClubMember.builder()
                .user(user)
                .club(club)
                .build()
        );

        clubHeadRepository.save(
                ClubHead.builder()
                .user(user)
                .club(club)
                .build()
        );

    }

    @AfterEach
    void deleteAll(){
//        feedRepository.deleteAll();
//        clubHeadRepository.deleteAll();
//        clubMemberRepository.deleteAll();
//        clubRepository.deleteAll();
//        userRepository.deleteAll();
    }

    @Test
    @Order(1)
    void uploadFeed() throws Exception {

        ContentRequest request = new ContentRequest("test");

        mvc.perform(post("/feed/1")
                .header("Authorization", "Bearer " + accessToken)
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    void getFeedList() throws Exception {
        mvc.perform(get("/feed/list")
                .param("page", String.valueOf(0)))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    void getClubFeedList() throws Exception {
        mvc.perform(get("/feed/1/list")
                .param("page", String.valueOf(0)))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    void feedModify() throws Exception {

		ContentRequest request = new ContentRequest("modify");

        mvc.perform(put("/feed/1")
                .header("Authorization", "Bearer " + accessToken)
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    void feedFlag() throws Exception {
        mvc.perform(put("/feed/1/flag")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    void getFeed() throws Exception {
        mvc.perform(get("/feed/1"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(7)
    void feedPin() throws Exception {
        mvc.perform(put("/feed/1/pin")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    @Order(8)
    void RefeedPin() throws Exception {
        uploadFeed();
        mvc.perform(put("/feed/2/pin")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    @Order(8)
    void deleteFeed() throws Exception {
        mvc.perform(delete("/feed/1")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    /*
    @Test
    @Order(9)
    public void fileUpload() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file",
                "test.png",
                MediaType.,
                "Hello, World!".getBytes());
        mvc.perform(post("/feed/2/medium")
                .header("Authorization", "Bearer " + accessToken)
                .queryParam("files", String.valueOf(multipartFile))
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated());
                
    }
    */

}
