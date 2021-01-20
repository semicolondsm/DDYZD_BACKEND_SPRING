package com.semicolon.spring.service.club_head;

import com.semicolon.spring.dto.ClubDTO;
import com.semicolon.spring.entity.club.Club;
import com.semicolon.spring.entity.club.ClubRepository;
import com.semicolon.spring.entity.club.major.Major;
import com.semicolon.spring.entity.club.major.MajorRepository;
import com.semicolon.spring.entity.user.User;
import com.semicolon.spring.exception.FileNotSaveException;
import com.semicolon.spring.exception.NoAuthorityException;
import com.semicolon.spring.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ClubHeadServiceImpl implements ClubHeadService{
    private final ClubRepository clubRepository;
    private final MajorRepository majorRepository;
    private final AuthenticationFacade authenticationFacade;

    @Value("${file.club.path}")
    private String PATH;

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

    @Override
    public ClubDTO.messageResponse clubProfile(MultipartFile file, int club_id) {
        if(!isClubHead(club_id))
            throw new NoAuthorityException();
        try{
            file.transferTo(new File(PATH+file.getOriginalFilename()));
            clubRepository.findById(club_id)
                    .map(club-> {
                        club.setProfile_image(PATH+file.getOriginalFilename());
                        clubRepository.save(club);
                        return club;
                    });
            return new ClubDTO.messageResponse("club profile write success");
        }catch (IOException e){
            e.printStackTrace();
            throw new FileNotSaveException();
        }
    }

    private boolean isClubHead(int club_id){
        User user = authenticationFacade.getUser();
        return clubRepository.findById(club_id)
                .map(club -> {
                    return user.getHead().contains(club.getClubHead());
                }).orElseThrow(NoAuthorityException::new);
    }
}
