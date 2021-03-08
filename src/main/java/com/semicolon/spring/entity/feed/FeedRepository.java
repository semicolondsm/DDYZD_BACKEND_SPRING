package com.semicolon.spring.entity.feed;

import com.semicolon.spring.entity.club.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface FeedRepository extends JpaRepository<Feed, Integer> {

    Page<Feed> findAllByOrderByIdAsc(Pageable pageable);

    Page<Feed> findByClub(Club club, Pageable pageable);
    List<Feed> findByClubAndPinIsTrue(Club club);
}
