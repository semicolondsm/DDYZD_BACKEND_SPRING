package com.semicolon.spring.service.application;

import com.semicolon.spring.dto.ApplicationDTO;
import com.semicolon.spring.entity.club.Club;
import com.semicolon.spring.entity.club.ClubRepository;
import com.semicolon.spring.entity.club.club_member.ClubMember;
import com.semicolon.spring.entity.club.club_member.ClubMemberRepository;
import com.semicolon.spring.entity.club.club_head.ClubHead;
import com.semicolon.spring.entity.club.club_head.ClubHeadRepository;
import com.semicolon.spring.entity.user.User;
import com.semicolon.spring.entity.user.UserRepository;
import com.semicolon.spring.exception.*;
import com.semicolon.spring.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final AuthenticationFacade authenticationFacade;
    private final ClubMemberRepository applicationRepository;
    private final ClubRepository clubRepository;
    private final ClubHeadRepository clubHeadRepository;
    private final UserRepository userRepository;


    @Override
    public ApplicationDTO.MessageResponse cancelApplication(int club_id) {
        User user = getUser();
        Club club = clubRepository.findById(club_id).orElseThrow(ClubNotFoundException::new);

        ClubMember application = applicationRepository.findByUserAndClub(user, club);

        if(application == null)
            throw new ApplicationNotFoundException();

        applicationRepository.deleteByUserAndClub(user, club);

        return new ApplicationDTO.MessageResponse("Application Cancel Success");
    }

    @Override
    public ApplicationDTO.MessageResponse removeApplication(int club_id, int user_id) {
        if(!isClubHead(club_id)){
            throw new NotClubHeadException();
        }

        User user = userRepository.findById(user_id).orElseThrow(UserNotFoundException::new);
        Club club = clubRepository.findById(club_id).orElseThrow(ClubNotFoundException::new);

        ClubMember application = applicationRepository.findByUserAndClub(user, club);

        if(application == null)
            throw new ApplicationNotFoundException();

        applicationRepository.deleteByUserAndClub(user, club);

        return new ApplicationDTO.MessageResponse("Application Remove Success");
    }

    @Override
    public ApplicationDTO.MessageResponse deportApplication(int club_id, int user_id) {
        if(!isClubHead(club_id)){
            throw new NotClubHeadException();
        }

        User user = userRepository.findById(user_id).orElseThrow(UserNotFoundException::new);
        Club club = clubRepository.findById(club_id).orElseThrow(ClubNotFoundException::new);

        if(user.getId().equals(getUser().getId())){
            throw new DontKickYourSelfException();
        }

        ClubMember application = applicationRepository.findByUserAndClub(user, club);

        if(application == null)
            throw new ApplicationNotFoundException();

        applicationRepository.deleteByUserAndClub(user, club);

        return new ApplicationDTO.MessageResponse("Club Member Deport Success");
    }


    private User getUser(){
        return authenticationFacade.getUser();
    }

    private boolean isClubHead(int club_id){
        User user = authenticationFacade.getUser();
        Club club = clubRepository.findById(club_id).orElseThrow(ClubNotFoundException::new);
        ClubHead clubHead = clubHeadRepository.findByClubAndUser(club, user);
        if(clubHead == null)
            throw new NotClubHeadException();
        else return true;
    }

}
