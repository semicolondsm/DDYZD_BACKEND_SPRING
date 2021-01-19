package com.semicolon.spring.entity.feed_flag;

import com.semicolon.spring.entity.feed.Feed;
import com.semicolon.spring.entity.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeedFlagRepository extends CrudRepository<FeedFlag, Integer> {
    Optional<FeedFlag> findByUserAndFeed(User User, Feed Feed);
    int countByFeed(Feed Feed);
}
