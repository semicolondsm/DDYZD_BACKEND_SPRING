package com.semicolon.spring.service.club_head;

import com.semicolon.spring.dto.ClubDTO;
import com.semicolon.spring.dto.HeadDTO;
import com.semicolon.spring.entity.club.Club;
import com.semicolon.spring.entity.club.ClubRepository;
import com.semicolon.spring.entity.club.club_follow.ClubFollow;
import com.semicolon.spring.entity.club.club_head.ClubHead;
import com.semicolon.spring.entity.club.club_head.ClubHeadRepository;
import com.semicolon.spring.entity.club.club_member.ClubMember;
import com.semicolon.spring.entity.club.club_member.ClubMemberRepository;
import com.semicolon.spring.entity.club.major.Major;
import com.semicolon.spring.entity.club.major.MajorRepository;
import com.semicolon.spring.entity.club.room.Room;
import com.semicolon.spring.entity.club.room.RoomRepository;
import com.semicolon.spring.entity.club.room.RoomStatus;
import com.semicolon.spring.entity.user.User;
import com.semicolon.spring.entity.user.UserRepository;
import com.semicolon.spring.exception.*;
import com.semicolon.spring.security.AuthenticationFacade;
import com.semicolon.spring.service.fcm.FcmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClubHeadServiceImpl implements ClubHeadService{
    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final ClubHeadRepository clubHeadRepository;
    private final MajorRepository majorRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final FcmService fcmService;
    private final AuthenticationFacade authenticationFacade;

    @Value("${file.club.path}")
    private String PATH;

    @Override
    public ClubDTO.messageResponse recruitment(ClubDTO.recruitment request, int club_id) {
        if(isClubHead(club_id)){
            throw new NotClubHeadException();
        }
        if(request.getCloseAt().before(new Date())){
            throw new BadRecruitmentTimeException();
        }
        if(request.getMajor().isEmpty())
            throw new BadRequestException();
        Club club = clubRepository.findById(club_id).orElseThrow(ClubNotFoundException::new);
        majorRepository.deleteByClub(club);
        club.setMajors();
        Set<String> majorList = new HashSet<>(request.getMajor());
        for(String major : majorList){
            majorRepository.save(
                    Major.builder()
                            .club(club)
                            .majorName(major)
                    .build()
            );
        }

        club.setStart_at();

        club.setClose_at(request.getCloseAt());
        clubRepository.save(club);

        for(Room room : roomRepository.findByClub(club)){
            if(clubMemberRepository.findByUserAndClub(room.getUser(), club).isEmpty() && room.getStatus().equals(RoomStatus.C)){
                room.setStatus("N");
                roomRepository.save(room);
            }
        }

        for(ClubFollow follow : club.getFollows()){
            User follower = follow.getUser();

            HeadDTO.FcmRequest fcmRequest = HeadDTO.FcmRequest.builder()
                    .token(follower.getDevice_token())
                    .title(club.getName())
                    .message(follower.getName() + "님, 팔로우하신 " + club.getName() + "동아리의 모집이 시작되었습니다.")
                    .club(club.getClubId())
                    .build();

            if(club.getName().equals("MoDeep")){
                fcmRequest.setMessage(follower.getName() + "님, 팔로우하신 " + club.getName() + "동아리의 모딥이 시작되었습니다.");
            }else{
                fcmRequest.setMessage(follower.getName() + "님, 팔로우하신 " + club.getName() + "동아리의 모집이 시작되었습니다.");
            }

            fcmService.send(fcmRequest);

        }

        log.info("make recruitment club_id : " + club_id);
        return new ClubDTO.messageResponse("recruitment success");
    }

    @Override
    public ClubDTO.messageResponse clubProfile(MultipartFile file, int club_id) {
        if(isClubHead(club_id))
            throw new NotClubHeadException();
        try{
            Random random = new Random(System.currentTimeMillis());
            String fileString = random.nextInt() + file.getOriginalFilename();
            file.transferTo(new File(PATH+ fileString));
            clubRepository.findById(club_id)
                    .map(club-> {
                        club.setProfile_image("club/" + fileString);
                        clubRepository.save(club);
                        return club;
                    });
            log.info("change club Profile club_id : " + club_id);
            return new ClubDTO.messageResponse("club profile write success");
        }catch (IOException e){
            e.printStackTrace();
            throw new FileSaveFailException();
        }
    }

    @Override
    public ClubDTO.messageResponse clubHongbo(MultipartFile file, int club_id) {
        if(isClubHead(club_id))
            throw new NotClubHeadException();
        try{
            Random random = new Random(System.currentTimeMillis());
            String fileString = random.nextInt() + file.getOriginalFilename();
            file.transferTo(new File(PATH+ fileString));
            clubRepository.findById(club_id)
                    .map(club -> {
                        club.setHongbo_image("club/" + fileString);
                        clubRepository.save(club);
                        return club;
                    });
            log.info("change club Hongbo club_id : " + club_id);
            return new ClubDTO.messageResponse("club hongbo write success");
        }catch (IOException e){
            e.printStackTrace();
            throw new FileSaveFailException();
        }
    }

    @Override
    public ClubDTO.messageResponse clubBanner(MultipartFile file, int club_id) {
        if(isClubHead(club_id))
            throw new NotClubHeadException();
        try{
            Random random = new Random(System.currentTimeMillis());
            String fileString = random.nextInt() + file.getOriginalFilename();
            file.transferTo(new File(PATH+ fileString));
            clubRepository.findById(club_id)
                    .map(club -> {
                        club.setBanner_image("club/" + fileString);
                        clubRepository.save(club);
                        return club;
                    });
            log.info("change club Banner club_id : " + club_id);
            return new ClubDTO.messageResponse("club banner write success");
        }catch (IOException e){
            e.printStackTrace();
            throw new FileSaveFailException();
        }
    }

    @Override
    public ClubDTO.messageResponse modifyClub(ClubDTO.modify request, int club_id) {
        if(isClubHead(club_id))
            throw new NotClubHeadException();
        clubRepository.findById(club_id)
                .map(club -> {
                    club.setClub_name(request.getClubName());
                    clubRepository.save(club);
                    return club;
                });
        log.info("change club name club_id : " + club_id);
        return new ClubDTO.messageResponse("club modify success");
    }

    @Override
    public ClubDTO.messageResponse changeHead(ClubDTO.changeHead request, int club_id) {
        if(isClubHead(club_id))
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
    public ClubDTO.messageResponse clubDescription(ClubDTO.description request, int club_id) {
        if(isClubHead(club_id))
            throw new NotClubHeadException();
        clubRepository.findById(club_id)
                .map(club -> {
                    club.setDescription(request.getDescription());
                    clubRepository.save(club);
                    return club;
                });
        log.info("change club description club_id : " + club_id);
        return new ClubDTO.messageResponse("description write success");
    }

    @Override
    public ClubDTO.messageResponse deleteRecruitment(int club_id) {
        if(isClubHead(club_id))
            throw new NotClubHeadException();
        clubRepository.findById(club_id)
                .map(club -> {
                    majorRepository.deleteByClub(club);
                    club.setStart_at(null);
                    club.setClose_at(null);

                    for(Room room : club.getRooms()){
                        if(room.getStatus().equals(RoomStatus.N)){
                            room.setStatus("C");
                            roomRepository.save(room);
                        }
                    }

                    for(ClubFollow user : club.getFollows()){
                        if(user.getUser().getDevice_token() != null){
                            HeadDTO.FcmRequest request = HeadDTO.FcmRequest.builder()
                                    .title(club.getName())
                                    .message(club.getName() + "의 모집공고가 취소되었습니다.")
                                    .token(user.getUser().getDevice_token())
                                    .club(club.getClubId())
                                    .build();
                            fcmService.send(request);
                        }
                    }
                    club.setMajors();

                    return clubRepository.save(club);
                }).orElseThrow(ClubNotFoundException::new);
        log.info("delete recruitment club_id : " + club_id);
        return new ClubDTO.messageResponse("delete recruitment success");
    }

    @Override
    public ClubDTO.hongbo getHongbo(int club_id) {
        if(isClubHead(club_id))
            throw new NotClubHeadException();
        return new ClubDTO.hongbo(clubRepository.findById(club_id)
                .map(Club::getHongbo_image).orElseThrow(ClubNotFoundException::new));
    }

    @Override
    public ClubDTO.messageResponse deleteHongbo(int club_id) {
        if(isClubHead(club_id))
            throw new NotClubHeadException();
        clubRepository.findById(club_id)
                .map(club -> {
                    club.setHongbo_image(null);
                    return clubRepository.save(club);
                });
        return new ClubDTO.messageResponse("delete hongbo success");
    }

    @Override
    public ClubDTO.messageResponse deportMember(int club_id, int user_id) {
        if(!isClubHead(club_id)){
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

    @Override
    public ClubDTO.messageResponse insertMember(int club_id, int user_id) {
        if(!isClubHead(club_id)){
            throw new NotClubHeadException();
        }

        User user = userRepository.findById(user_id).orElseThrow(UserNotFoundException::new);
        Club club = clubRepository.findById(club_id).orElseThrow(ClubNotFoundException::new);

        if(clubMemberRepository.findByUserAndClub(user, club).isPresent()){
            throw new AlreadyClubMember();
        }

        clubMemberRepository.save(ClubMember.builder()
                .club(club)
                .user(user)
                .build()
        );

        return new ClubDTO.messageResponse("Insert Club Member Success");

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
        else return false;
    }
}
