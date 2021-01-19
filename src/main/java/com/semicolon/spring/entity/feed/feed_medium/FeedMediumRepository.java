package com.semicolon.spring.entity.feed.feed_medium;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedMediumRepository extends CrudRepository<FeedMedium, Integer> {
}
