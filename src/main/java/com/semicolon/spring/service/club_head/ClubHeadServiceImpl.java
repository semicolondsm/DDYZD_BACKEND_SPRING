package com.semicolon.spring.service.club_head;

import com.semicolon.spring.dto.ClubDTO;
import com.semicolon.spring.entity.club.Club;
import com.semicolon.spring.entity.club.ClubRepository;
import com.semicolon.spring.entity.club.club_head.ClubHead;
import com.semicolon.spring.entity.club.club_head.ClubHeadRepository;
import com.semicolon.spring.entity.club.major.Major;
import com.semicolon.spring.entity.club.major.MajorRepository;
import com.semicolon.spring.entity.user.User;
import com.semicolon.spring.entity.user.UserRepository;
import com.semicolon.spring.exception.NoAuthorityException;
import com.semicolon.spring.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubHeadServiceImpl implements ClubHeadService{
    private final ClubRepository clubRepository;
    private final MajorRepository majorRepository;
    private final AuthenticationFacade authenticationFacade;

    @Override
    public ClubDTO.messageResponse recruitment(ClubDTO.recruitment request, int club_id) {
        if(!isClubHead(club_id)){
            throw new NoAuthorityException();
        }
        Club club = clubRepository.findByClubId(club_id);
        for(String major : request.getMajor()){
            majorRepository.save(
                    Major.builder()
                            .club(club)
                            .majorName(major)
                    .build()
            );
        }
        club.setClose_at(request.getCloseAt());
        clubRepository.save(club);
        return new ClubDTO.messageResponse("recruitment success");

    }

    private boolean isClubHead(int club_id){
        User user = authenticationFacade.getUser();
        return clubRepository.findById(club_id)
                .map(club -> {
                    return user.getHead().contains(club.getClubHead());
                }).orElseThrow(NoAuthorityException::new);
    }
}
