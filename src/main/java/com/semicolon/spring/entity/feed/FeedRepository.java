package com.semicolon.spring.entity.feed;

import com.semicolon.spring.entity.club.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FeedRepository extends CrudRepository<Feed, Integer> {
    Page<Feed> findAll(Pageable pageable);
    Page<Feed> findByClubId(Club clubId, Pageable pageable);
}
