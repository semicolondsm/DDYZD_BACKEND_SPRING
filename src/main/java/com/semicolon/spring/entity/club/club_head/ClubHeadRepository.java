package com.semicolon.spring.entity.club.club_head;

import com.semicolon.spring.entity.club.Club;
import com.semicolon.spring.entity.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClubHeadRepository extends CrudRepository<ClubHead, Integer> {
    List<ClubHead> findByUser(User user);
    List<ClubHead> findByClub(Club club);
    Optional<ClubHead> findByClubAndUser(Club club, User user);
}
