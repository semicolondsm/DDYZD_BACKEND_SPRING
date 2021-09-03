package com.semicolon.spring.service.club;

import com.semicolon.spring.dto.club.response.ClubResponse;
import com.semicolon.spring.dto.club.response.FollowersResponse;
import com.semicolon.spring.entity.club.Club;
import com.semicolon.spring.entity.club.ClubRepository;
import com.semicolon.spring.entity.user.User;
import com.semicolon.spring.entity.user.UserRepository;
import com.semicolon.spring.exception.ClubNotFoundException;
import com.semicolon.spring.exception.UserNotFoundException;
import com.semicolon.spring.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {

    private final ClubRepository clubRepository;
    private final UserRepository userRepository;
    private final AuthenticationFacade authenticationFacade;


    @Override
    public FollowersResponse getFollowers(int clubId) {
        return new FollowersResponse(clubRepository.findById(clubId)
                .map(club -> club.getFollows().size())
                .orElseThrow(ClubNotFoundException::new));
    }

    @Override
    public List<ClubResponse> getClubs() {
        User user = authenticationFacade.getUser();
        List<ClubResponse> clubList = new ArrayList<>();

        return userRepository.findById(user.getId())
                .map(foundUser -> {
                    foundUser.getFollows().forEach(clubFollow -> {
                        Club club = clubFollow.getClub();
                        clubList.add(ClubResponse.builder()
                                .clubId(club.getClubId())
                                .clubName(club.getName())
                                .clubImage(club.getProfileImage())
                                .clubDescription(club.getDescription())
                                .build()
                        );
                    });
                    return clubList;
                }).orElseThrow(UserNotFoundException::new);
    }
}
