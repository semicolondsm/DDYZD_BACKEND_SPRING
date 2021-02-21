package com.semicolon.spring.entity.club.major;

import com.semicolon.spring.entity.club.Club;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface MajorRepository extends CrudRepository<Major, Integer> {
    @Transactional
    void deleteByClub(Club club);
}
