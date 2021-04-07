package com.semicolon.spring.entity.club.club_manager;

import com.semicolon.spring.entity.club.Club;
import com.semicolon.spring.entity.user.User;
import org.springframework.data.repository.CrudRepository;

public interface ClubManagerRepository extends CrudRepository<ClubManager, Integer> {
    ClubManager findByClubAndUser(Club club, User user);
}
