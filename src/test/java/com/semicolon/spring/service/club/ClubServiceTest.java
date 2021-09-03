package com.semicolon.spring.service.club;

import com.semicolon.spring.dto.club.response.FollowersResponse;
import com.semicolon.spring.entity.club.Club;
import com.semicolon.spring.entity.club.ClubRepository;
import com.semicolon.spring.entity.user.User;
import com.semicolon.spring.entity.user.UserRepository;
import com.semicolon.spring.exception.ClubNotFoundException;
import com.semicolon.spring.exception.UserNotFoundException;
import com.semicolon.spring.security.AuthenticationFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClubServiceTest {
    @Mock
    private ClubRepository clubRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationFacade authenticationFacade;

    private ClubService clubService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        clubService = new ClubServiceImpl(clubRepository, userRepository, authenticationFacade);
    }

    @Test
    public void getFollowers_success() {
        int clubId = 1;

        when(clubRepository.findById(clubId))
                .thenReturn(Optional.of(Club.builder().build()));

        assertEquals(new FollowersResponse(0).getFollowers(), clubService.getFollowers(clubId).getFollowers());
    }

    @Test
    public void getFollowers_cannot_find_club() {
        int clubId = 1;

        when(clubRepository.findById(clubId))
                .thenReturn(Optional.empty());

        assertThrows(ClubNotFoundException.class, () -> clubService.getFollowers(clubId));
    }

    @Test
    public void getClubs_success() {
        User user = User.builder()
                .id(1)
                .build();

        when(authenticationFacade.getUser())
                .thenReturn(user);

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        assertTrue(clubService.getClubs().isEmpty());
    }

    @Test
    public void getClubs_user_not_found() {
        User user = User.builder()
                .id(1)
                .build();

        when(authenticationFacade.getUser())
                .thenReturn(user);

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> clubService.getClubs());
    }

    /* Business Logic is needed to fix
    @Test
    public void getClubs_auth_user_not_found() {
        when(authenticationFacade.getUser())
                .thenReturn(null);

        when(userRepository.findById(null))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> clubService.getClubs());
    }
    */
}
