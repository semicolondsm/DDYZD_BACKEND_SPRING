package com.semicolon.spring.entity.feed_flag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "feed_flag")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedFlag {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer flag_id;


}
