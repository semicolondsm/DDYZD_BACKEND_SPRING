package com.semicolon.spring.entity.feed;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedRepository extends CrudRepository<Feed, Integer> {
    List<Feed> findByIdLessThanOrderByIdDesc(int Id);
}
