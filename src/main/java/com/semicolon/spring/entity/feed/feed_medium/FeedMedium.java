package com.semicolon.spring.entity.feed.feed_medium;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.semicolon.spring.entity.feed.Feed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "feed_medium")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class FeedMedium {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String medium_path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    @JsonBackReference
    private Feed feed;

}
