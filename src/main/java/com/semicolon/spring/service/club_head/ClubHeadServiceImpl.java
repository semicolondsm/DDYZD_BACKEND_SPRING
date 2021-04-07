package com.semicolon.spring.service.club_head;

import com.semicolon.spring.dto.ClubDTO;
import com.semicolon.spring.dto.HeadDTO;
import com.semicolon.spring.entity.club.Club;
import com.semicolon.spring.entity.club.ClubRepository;
import com.semicolon.spring.entity.club.club_head.ClubHead;
import com.semicolon.spring.entity.club.club_head.ClubHeadRepository;
import com.semicolon.spring.entity.club.club_manager.ClubManager;
import com.semicolon.spring.entity.club.club_manager.ClubManagerRepository;
import com.semicolon.spring.entity.club.club_member.ClubMember;
import com.semicolon.spring.entity.club.club_member.ClubMemberRepository;
import com.semicolon.spring.entity.user.User;
import com.semicolon.spring.entity.user.UserRepository;
import com.semicolon.spring.exception.*;
import com.semicolon.spring.security.AuthenticationFacade;
import com.semicolon.spring.service.fcm.FcmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClubHeadServiceImpl implements ClubHeadService{
    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final ClubHeadRepository clubHeadRepository;
    private final ClubManagerRepository clubManagerRepository;

    private final UserRepository userRepository;

    private final FcmService fcmService;
    private final AuthenticationFacade authenticationFacade;

    @Override
    public ClubDTO.messageResponse insertMember(int club_id, int user_id) {
        if(isNotClubHead(club_id)){
            throw new NotClubHeadException();
        }

        User user = userRepository.findById(user_id).orElseThrow(UserNotFoundException::new);
        Club club = clubRepository.findById(club_id).orElseThrow(ClubNotFoundException::new);

        if(clubMemberRepository.findByUserAndClub(user, club).isPresent()){
            throw new AlreadyClubMemberException();
        }

        clubMemberRepository.save(ClubMember.builder()
                .club(club)
                .user(user)
                .build()
        );

        return new ClubDTO.messageResponse("Insert Club Member Success");

    }

    @Override
    public ClubDTO.messageResponse changeHead(ClubDTO.changeHead request, int club_id) {
        if(isNotClubHead(club_id))
            throw new NotClubHeadException();
        clubRepository.findById(club_id)
                .map(club -> {
                    ClubHead clubHead = club.getClubHead();
                    clubHead.setUser(userRepository.findByGcn(request.getUserGcn()));
                    clubHeadRepository.save(clubHead);
                    return club;
                });
        log.info("change Head club_id : " + club_id);
        return new ClubDTO.messageResponse("club head change success");
    }

    @Override
    public ClubDTO.messageResponse insertManager(int club_id, int user_id) {
        if(isNotClubHead(club_id)){
            throw new NotClubHeadException();
        }

        User user = userRepository.findById(user_id).orElseThrow(UserNotFoundException::new);
        Club club = clubRepository.findById(club_id).orElseThrow(ClubNotFoundException::new);

        if(clubManagerRepository.findByClubAndUser(club, user).isPresent()){
            throw new AlreadyClubManagerException();
        }

        clubManagerRepository.save(
                ClubManager.builder()
                .club(club)
                .user(user)
                .build()
        );

        return new ClubDTO.messageResponse("Insert Club Manager Success");

    }

    @Override
    public ClubDTO.messageResponse deportManager(int club_id, int user_id) {
        if(isNotClubHead(club_id)){
            throw new NotClubHeadException();
        }

        User user = userRepository.findById(user_id).orElseThrow(UserNotFoundException::new);
        Club club = clubRepository.findById(club_id).orElseThrow(ClubNotFoundException::new);

        if(user.getId().equals(getUser().getId())){
            throw new DontKickYourSelfException();
        }

        clubManagerRepository.findByClubAndUser(club, user).orElseThrow(NotClubManagerException::new);

        clubManagerRepository.deleteByClubAndUser(club, user);

        return new ClubDTO.messageResponse("Club Manager Deport Success");
    }

    @Override
    public ClubDTO.messageResponse deportMember(int club_id, int user_id) {
        if(isNotClubHead(club_id)){
            throw new NotClubHeadException();
        }

        User user = userRepository.findById(user_id).orElseThrow(UserNotFoundException::new);
        Club club = clubRepository.findById(club_id).orElseThrow(ClubNotFoundException::new);

        if(user.getId().equals(getUser().getId())){
            throw new DontKickYourSelfException();
        }

        clubMemberRepository.findByUserAndClub(user, club).orElseThrow(NotClubMemberException::new);

        clubMemberRepository.deleteByUserAndClub(user, club);

        HeadDTO.FcmRequest request = HeadDTO.FcmRequest.builder()
                .token(user.getDevice_token())
                .title(club.getName())
                .message(user.getName() + "님이 " + club.getName() + "에서 추방당하셨습니다.")
                .club(club.getClubId())
                .build();

        fcmService.send(request);

        return new ClubDTO.messageResponse("Club Member Deport Success");
    }

    private User getUser(){
        return authenticationFacade.getUser();
    }

    private boolean isNotClubHead(int club_id){
        User user = authenticationFacade.getUser();
        Club club = clubRepository.findById(club_id).orElseThrow(ClubNotFoundException::new);
        clubHeadRepository.findByClubAndUser(club, user).orElseThrow(NotClubHeadException::new);
        return false;
    }

}
