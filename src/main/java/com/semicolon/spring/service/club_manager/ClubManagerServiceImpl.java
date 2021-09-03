package com.semicolon.spring.service.club_manager;

import com.semicolon.spring.dto.HeadDTO;
import com.semicolon.spring.dto.club.request.ClubNameModifyRequest;
import com.semicolon.spring.dto.club.request.DescriptionRequest;
import com.semicolon.spring.dto.club.request.RecruitmentRequest;
import com.semicolon.spring.dto.club.response.HongboResponse;
import com.semicolon.spring.entity.club.Club;
import com.semicolon.spring.entity.club.ClubRepository;
import com.semicolon.spring.entity.club.club_follow.ClubFollow;
import com.semicolon.spring.entity.club.club_manager.ClubManagerRepository;
import com.semicolon.spring.entity.club.club_member.ClubMemberRepository;
import com.semicolon.spring.entity.club.major.Major;
import com.semicolon.spring.entity.club.major.MajorRepository;
import com.semicolon.spring.entity.club.room.Room;
import com.semicolon.spring.entity.club.room.RoomRepository;
import com.semicolon.spring.entity.club.room.RoomStatus;
import com.semicolon.spring.entity.user.User;
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
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClubManagerServiceImpl implements ClubManagerService {

    public static final String CLUB_PATH = "club/";

    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final ClubManagerRepository clubManagerRepository;

    private final MajorRepository majorRepository;
    private final RoomRepository roomRepository;

    private final FcmService fcmService;
    private final AuthenticationFacade authenticationFacade;

    @Value("${file.club.path}")
    private String path;

    @Override
    public void recruitment(RecruitmentRequest request, int clubId) {
        isNotClubManager(clubId);
        if(request.getCloseAt().before(new Date())){
            throw new BadRecruitmentTimeException();
        }
        if(request.getMajor().isEmpty())
            throw new BadRequestException();
        var club = clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new);
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

        club.setStartAt();

        club.setCloseAt(request.getCloseAt());
        clubRepository.save(club);

        for(Room room : roomRepository.findByClub(club)){
            if(clubMemberRepository.findByUserAndClub(room.getUser(), club).isEmpty() && room.getStatus().equals(RoomStatus.C)){
                room.setStatus("N");
                roomRepository.save(room);
            }
        }

        for(ClubFollow follow : club.getFollows()){
            var follower = follow.getUser();

            var fcmRequest = HeadDTO.FcmRequest.builder()
                    .token(follower.getDeviceToken())
                    .title(club.getName())
                    .message(follower.getName() + "님, 팔로우하신 " + club.getName() + "동아리의 모집이 시작되었습니다.")
                    .club(club.getClubId())
                    .build();

            if(club.getName().equals("MoDeep")){
                fcmRequest.setMessage(follower.getName() + "님, 팔로우하신 모집 동아리의 모딥이 시작되었습니다.");
            }else{
                fcmRequest.setMessage(follower.getName() + "님, 팔로우하신 " + club.getName() + "동아리의 모집이 시작되었습니다.");
            }

            fcmService.send(fcmRequest);

        }
    }

    @Override
    public void clubProfile(MultipartFile file, int clubId) {
        isNotClubManager(clubId);
        try{
            var random = new Random(System.currentTimeMillis());
            var fileString = random.nextInt() + file.getOriginalFilename();
            file.transferTo(new File(path + fileString));
            clubRepository.findById(clubId)
                    .map(club-> clubRepository.save(club.setProfileImage(CLUB_PATH + fileString)));
        }catch (IOException e){
            e.printStackTrace();
            throw new FileSaveFailException();
        }
    }

    @Override
    public void clubHongbo(MultipartFile file, int clubId) {
        isNotClubManager(clubId);
        try{
            var random = new Random(System.currentTimeMillis());
            var fileString = random.nextInt() + file.getOriginalFilename();
            file.transferTo(new File(path + fileString));
            clubRepository.findById(clubId)
                    .map(club -> clubRepository.save(club.setHongboImage(CLUB_PATH + fileString)));
        }catch (IOException e){
            e.printStackTrace();
            throw new FileSaveFailException();
        }
    }

    @Override
    public void clubBanner(MultipartFile file, int clubId) {
        isNotClubManager(clubId);
        try{
            var random = new Random(System.currentTimeMillis());
            var fileString = random.nextInt() + file.getOriginalFilename();
            file.transferTo(new File(path + fileString));
            clubRepository.findById(clubId)
                    .map(club -> clubRepository.save(club.setBannerImage(CLUB_PATH + fileString)));
        }catch (IOException e){
            e.printStackTrace();
            throw new FileSaveFailException();
        }
    }

    @Override
    public void modifyClub(ClubNameModifyRequest request, int clubId) {
        isNotClubManager(clubId);
        clubRepository.findById(clubId)
                .map(club -> clubRepository.save(club.setClubName(request.getClubName())));
    }



    @Override
    public void clubDescription(DescriptionRequest request, int clubId) {
        isNotClubManager(clubId);
        clubRepository.findById(clubId)
                .map(club -> clubRepository.save(club.setDescription(request.getDescription())));
    }

    @Override
    public void deleteRecruitment(int clubId) {
        isNotClubManager(clubId);
        clubRepository.findById(clubId)
                .map(club -> {
                    majorRepository.deleteByClub(club);
                    club.setStartAt(null);
                    club.setCloseAt(null);

                    for(Room room : club.getRooms()){
                        if(room.getStatus().equals(RoomStatus.N)){
                            room.setStatus("C");
                            roomRepository.save(room);
                        }
                    }

                    for(ClubFollow user : club.getFollows()){
                        if(user.getUser().getDeviceToken() != null){
                            HeadDTO.FcmRequest request = HeadDTO.FcmRequest.builder()
                                    .title(club.getName())
                                    .message(club.getName() + "의 모집공고가 취소되었습니다.")
                                    .token(user.getUser().getDeviceToken())
                                    .club(club.getClubId())
                                    .build();
                            fcmService.send(request);
                        }
                    }
                    club.setMajors();

                    return clubRepository.save(club);
                }).orElseThrow(ClubNotFoundException::new);
    }

    @Override
    public HongboResponse getHongbo(int clubId) {
        isNotClubManager(clubId);
        return new HongboResponse(clubRepository.findById(clubId)
                .map(Club::getHongboImage).orElseThrow(ClubNotFoundException::new));
    }

    @Override
    public void deleteHongbo(int clubId) {
        isNotClubManager(clubId);
        clubRepository.findById(clubId)
                .map(club -> clubRepository.save(club.setHongboImage(null)));
    }

    private void isNotClubManager(int clubId){
        User user = authenticationFacade.getUser();
        Club club = clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new);
        clubManagerRepository.findByClubAndUser(club, user).orElseThrow(NotClubManagerException::new);
    }
}
