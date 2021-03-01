package com.semicolon.spring.service.application;

import com.semicolon.spring.dto.HeadDTO;
import com.semicolon.spring.entity.club.Club;
import com.semicolon.spring.entity.club.ClubRepository;
import com.semicolon.spring.entity.club.club_member.ClubMember;
import com.semicolon.spring.entity.club.club_member.ClubMemberRepository;
import com.semicolon.spring.entity.club.club_head.ClubHead;
import com.semicolon.spring.entity.club.club_head.ClubHeadRepository;
import com.semicolon.spring.entity.club.room.Room;
import com.semicolon.spring.entity.club.room.RoomRepository;
import com.semicolon.spring.entity.club.room.RoomStatus;
import com.semicolon.spring.entity.user.User;
import com.semicolon.spring.entity.user.UserRepository;
import com.semicolon.spring.exception.*;
import com.semicolon.spring.security.AuthenticationFacade;
import com.semicolon.spring.service.fcm.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final AuthenticationFacade authenticationFacade;
    private final FcmService fcmService;
    private final ClubMemberRepository clubMemberRepository;
    private final ClubRepository clubRepository;
    private final ClubHeadRepository clubHeadRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;


    @Override
    public HeadDTO.MessageResponse cancelApplication(int club_id) throws ExecutionException, InterruptedException {
        User user = getUser();
        Club club = clubRepository.findById(club_id).orElseThrow(ClubNotFoundException::new);

        deleteApplication(user, club);

        return new HeadDTO.MessageResponse("Application Cancel Success");
    }

    @Override
    public HeadDTO.MessageResponse removeApplication(int club_id, int user_id) throws ExecutionException, InterruptedException {
        if(!isClubHead(club_id)){
            throw new NotClubHeadException();
        }

        User user = userRepository.findById(user_id).orElseThrow(UserNotFoundException::new);
        Club club = clubRepository.findById(club_id).orElseThrow(ClubNotFoundException::new);

        deleteApplication(user, club);

        return new HeadDTO.MessageResponse("Application Remove Success");
    }

    @Override
    public HeadDTO.MessageResponse deportApplication(int club_id, int user_id) {
        if(!isClubHead(club_id)){
            throw new NotClubHeadException();
        }

        User user = userRepository.findById(user_id).orElseThrow(UserNotFoundException::new);
        Club club = clubRepository.findById(club_id).orElseThrow(ClubNotFoundException::new);

        if(user.getId().equals(getUser().getId())){
            throw new DontKickYourSelfException();
        }

        ClubMember application = clubMemberRepository.findByUserAndClub(user, club);

        if(application == null)
            throw new ApplicationNotFoundException();

        clubMemberRepository.deleteByUserAndClub(user, club);

        return new HeadDTO.MessageResponse("Club Member Deport Success");
    }

    private void deleteApplication(User user, Club club) throws ExecutionException, InterruptedException {
        Room application = roomRepository.findByUserAndClub(user, club);

        if(application == null)
            throw new ApplicationNotFoundException();

        roomRepository.save(Room.builder()
                .club(application.getClub())
                .club_looked(application.isClub_looked())
                .user_looked(application.isUser_looked())
                .status(RoomStatus.C)
                .user(application.getUser())
                .id(application.getId())
                .build()
        );

        if(user.getDevice_token()!=null){
            HeadDTO.FcmRequest request = HeadDTO.FcmRequest.builder()
                    .token(user.getDevice_token())
                    .title("대동여지도")
                    .message(user.getName() + "님의 " + application.getClub().getName() + "동아리 신청이 취소되었습니다.")
                    .build();
            fcmService.send(request);
        }
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
