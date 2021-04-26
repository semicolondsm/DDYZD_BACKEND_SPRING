package com.semicolon.spring.service.club;

import com.semicolon.spring.dto.ClubDTO.*;
import com.semicolon.spring.entity.club.ClubRepository;
import com.semicolon.spring.entity.club.club_follow.ClubFollow;
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
    public Followers getFollowers(int clubId) {
        return new Followers(clubRepository.findById(clubId)
                .map(club -> club.getFollows().size())
                .orElseThrow(ClubNotFoundException::new));
    }

    @Override
    public List<Club> getClubs() {
        User user = authenticationFacade.getUser();
        List<Club> clubList = new ArrayList<>();
        return userRepository.findById(user.getId())
                .map(foundUser -> {
                    for(ClubFollow clubFollow : foundUser.getFollows()){
                        com.semicolon.spring.entity.club.Club club = clubFollow.getClub();
                        clubList.add(Club.builder()
                                .clubId(club.getClubId())
                                .clubName(club.getName())
                                .clubImage(club.getProfileImage())
                                .clubDescription(club.getDescription())
                                .build()
                        );
                    }
                    return clubList;
                }).orElseThrow(UserNotFoundException::new);
    }
}
