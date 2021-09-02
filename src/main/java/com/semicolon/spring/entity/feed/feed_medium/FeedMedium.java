package com.semicolon.spring.entity.feed.feed_medium;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.semicolon.spring.entity.feed.Feed;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "feed_medium")
public class FeedMedium {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String mediumPath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    @JsonBackReference
    private Feed feed;


    @Builder
    public FeedMedium(String mediumPath, Feed feed) {
        this.mediumPath = mediumPath;
        this.feed = feed;
    }

}
