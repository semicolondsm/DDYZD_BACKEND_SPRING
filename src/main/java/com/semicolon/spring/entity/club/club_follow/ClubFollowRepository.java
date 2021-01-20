package com.semicolon.spring.entity.club.club_follow;

import com.semicolon.spring.entity.club.Club;
import com.semicolon.spring.entity.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClubFollowRepository extends CrudRepository<ClubFollow, Integer> {
    Optional<ClubFollow> findByUserAndClub(User user, Club club);
}
