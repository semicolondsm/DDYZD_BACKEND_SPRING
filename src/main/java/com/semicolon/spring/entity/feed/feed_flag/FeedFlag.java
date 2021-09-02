package com.semicolon.spring.entity.feed.feed_flag;

import com.semicolon.spring.entity.feed.Feed;
import com.semicolon.spring.entity.user.User;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"feed_id", "user_id"}))
@Entity(name = "feed_flag")
public class FeedFlag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public FeedFlag(Feed feed, User user) {
        this.feed = feed;
        this.user = user;
    }

}
