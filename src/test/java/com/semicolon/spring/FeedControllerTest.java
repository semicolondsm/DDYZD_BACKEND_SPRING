package com.semicolon.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.semicolon.spring.dto.FeedDTO;
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

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FeedControllerTest {

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

    Club club;

    User user;

    @BeforeEach
    public void setup(){
        //mvc = webAppContextSetup(context).build();

        user = userRepository.save(
                User.builder()
                .id(1)
                .imagePath("test")
                .name("세미콜론")
                .gcn("0000")
                .build()
        );

        accessToken = jwtTokenProvider.generateAccessToken(1);

        club = clubRepository.save(
                Club.builder()
                .clubId(1)
                .name("SEMICOLON;")
                .total_budget(0)
                .current_budget(0)
                .banner_image("a")
                .profile_image("a")
                .hongbo_image("a")
                .build()
        );

        System.out.println(club.getClubId());

        clubMemberRepository.save(
                ClubMember.builder()
                .user(user)
                .club(club)
                .build()
        );

        clubHeadRepository.save(
                ClubHead.builder()
                        .club(club)
                        .user(user)
                        .build()
        );

    }

    @AfterEach
    public void deleteAll(){
        userRepository.deleteAll();
        clubRepository.deleteAll();
        clubHeadRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void uploadFeed() throws Exception {

        FeedDTO.Feed request = new FeedDTO.Feed("test");

        mvc.perform(post("/feed/" + club.getClubId())
                .header("Authorization", "Bearer " + accessToken)
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    public void getFeedList() throws Exception {
        mvc.perform(get("/feed/list")
                .param("page", String.valueOf(0)))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void getClubFeedList() throws Exception {
        mvc.perform(get("/feed/" + club.getClubId() + "/list")
                .param("page", String.valueOf(0)))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    public void feedModify() throws Exception {

        FeedDTO.Feed request = new FeedDTO.Feed("modify");

        mvc.perform(put("/feed/1")
                .header("Authorization", "Bearer " + accessToken)
                .content(new ObjectMapper().writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    public void feedFlag() throws Exception {
        mvc.perform(put("/feed/1/flag")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    public void getFeed() throws Exception {
        mvc.perform(get("/feed/1"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(7)
    public void feedPin() throws Exception {
        mvc.perform(put("/feed/1/pin")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

    @Test
    @Order(8)
    public void deleteFeed() throws Exception {
        mvc.perform(delete("/feed/1")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());
    }

}
