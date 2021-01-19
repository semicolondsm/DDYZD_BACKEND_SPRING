package com.semicolon.spring.entity.club.major;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MajorRepository extends CrudRepository<Major, Integer> {
}
