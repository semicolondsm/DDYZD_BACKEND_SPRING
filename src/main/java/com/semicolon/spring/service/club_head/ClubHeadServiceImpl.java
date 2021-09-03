package com.semicolon.spring.service.club_head;

import com.semicolon.spring.dto.HeadDTO;
import com.semicolon.spring.dto.club.request.ChangeHeadRequest;
import com.semicolon.spring.dto.club.response.InformationResponse;
import com.semicolon.spring.entity.club.Club;
import com.semicolon.spring.entity.club.ClubRepository;
import com.semicolon.spring.entity.club.activity_detail.Activity;
import com.semicolon.spring.entity.club.activity_detail.ActivityDetail;
import com.semicolon.spring.entity.club.activity_detail.ActivityDetailRepository;
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
    private final ActivityDetailRepository activityDetailRepository;

    @Override
    public void changeHead(ChangeHeadRequest request, int clubId) {
        isNotClubHead(clubId);
        clubRepository.findById(clubId)
                .map(club -> clubHeadRepository.save(club.getClubHead().setUser(userRepository.findByGcn(request.getUserGcn()))));

    }

    @Override
    public void insertMember(int clubId, int userId) {
        isNotClubHead(clubId);

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Club club = clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new);

        if(clubMemberRepository.findByUserAndClub(user, club).isPresent()){
            throw new AlreadyClubMemberException();
        }

        clubMemberRepository.save(ClubMember.builder()
                .club(club)
                .user(user)
                .build()
        );

        activityDetailRepository.save(ActivityDetail.builder()
                .club(club)
                .user(user)
                .activity(Activity.JOIN)
                .build()
        );
    }

    @Override
    public void deportMember(int clubId, int userId) {
        InformationResponse information = checkDeport(clubId, userId);

        var user = information.getUser();
        var club = information.getClub();

        clubMemberRepository.findByUserAndClub(user, club).orElseThrow(ClubMemberNotFound::new);

        clubMemberRepository.deleteByUserAndClub(user, club);

        HeadDTO.FcmRequest request = HeadDTO.FcmRequest.builder()
                .token(user.getDeviceToken())
                .title(club.getName())
                .message(user.getName() + "님이 " + club.getName() + "에서 추방당하셨습니다.")
                .club(club.getClubId())
                .build();

        activityDetailRepository.save(ActivityDetail.builder()
                .club(club)
                .user(user)
                .activity(Activity.DEPORT)
                .build()
        );

        fcmService.send(request);
    }

    @Override
    public void insertManager(int clubId, int userId) {
        isNotClubHead(clubId);

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Club club = clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new);

        if(clubManagerRepository.findByClubAndUser(club, user).isPresent()){
            throw new AlreadyClubManagerException();
        }

        clubManagerRepository.save(
                ClubManager.builder()
                .club(club)
                .user(user)
                .build()
        );
    }

    @Override
    public void deportManager(int clubId, int userId) {
        InformationResponse information = checkDeport(clubId, userId);

        User user = information.getUser();
        Club club = information.getClub();

        clubManagerRepository.findByClubAndUser(club, user).orElseThrow(ClubManagerNotFound::new);

        clubManagerRepository.deleteByClubAndUser(club, user);
    }

    private User getUser(){
        return authenticationFacade.getUser();
    }

    private void isNotClubHead(int clubId){
        User user = authenticationFacade.getUser();
        Club club = clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new);
        clubHeadRepository.findByClubAndUser(club, user).orElseThrow(NotClubHeadException::new);
    }

    private InformationResponse checkDeport(int clubId, int userId){
        isNotClubHead(clubId);

        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        var club = clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new);

        if(user.getId().equals(getUser().getId())){
            throw new DontKickYourSelfException();
        }
        return new InformationResponse(user, club);
    }

}
