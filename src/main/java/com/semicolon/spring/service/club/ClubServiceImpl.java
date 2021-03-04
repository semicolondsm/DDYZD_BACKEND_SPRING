package com.semicolon.spring.service.club;

import com.semicolon.spring.dto.ClubDTO;
import com.semicolon.spring.entity.club.ClubRepository;
import com.semicolon.spring.exception.ClubNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService {

    private final ClubRepository clubRepository;


    @Override
    public ClubDTO.followers getFollowers(int club_id) {
        return new ClubDTO.followers(clubRepository.findById(club_id)
                .map(club -> club.getFollows().size())
                .orElseThrow(ClubNotFoundException::new));
    }
}
