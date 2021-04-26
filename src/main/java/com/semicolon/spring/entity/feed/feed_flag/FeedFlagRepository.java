package com.semicolon.spring.entity.feed.feed_flag;

import com.semicolon.spring.entity.feed.Feed;
import com.semicolon.spring.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedFlagRepository extends CrudRepository<FeedFlag, Integer> {
    Optional<FeedFlag> findByUserAndFeed(User User, Feed Feed);
    Page<FeedFlag> findByUser(User user, Pageable pageable);
    int countByFeed(Feed Feed);
}
