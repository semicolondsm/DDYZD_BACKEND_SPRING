package com.semicolon.spring.entity.club.application;

import com.semicolon.spring.entity.club.Club;
import com.semicolon.spring.entity.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ApplicationRepository extends CrudRepository<Application, Integer> {
    Application findByUserAndClub(User user, Club club);
}
