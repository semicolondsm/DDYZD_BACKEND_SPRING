package com.semicolon.spring.entity.club.club_member;

import com.semicolon.spring.entity.club.Club;
import com.semicolon.spring.entity.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


@Repository
public interface ClubMemberRepository extends CrudRepository<ClubMember, Integer> {
    ClubMember findByUserAndClub(User user, Club club);
    @Transactional
    void deleteByUserAndClub(User user, Club club);
}
