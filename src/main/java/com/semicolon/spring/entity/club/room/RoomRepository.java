package com.semicolon.spring.entity.club.room;

import com.semicolon.spring.entity.club.Club;
import com.semicolon.spring.entity.user.User;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface RoomRepository extends CrudRepository<Room, Integer> {
    Room findByUserAndClub(User user, Club club);

    @Transactional
    void deleteByUserAndClub(User user, Club club);

}
