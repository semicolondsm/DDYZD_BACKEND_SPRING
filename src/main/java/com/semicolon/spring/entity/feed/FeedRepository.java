package com.semicolon.spring.entity.feed;

import com.semicolon.spring.entity.club.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FeedRepository extends CrudRepository<Feed, Integer> {
    Page<Feed> findAll(Pageable pageable);
    Page<Feed> findByClub(Club club, Pageable pageable);
    List<Feed> findByClubAndPinIsTrue(Club club);
}
