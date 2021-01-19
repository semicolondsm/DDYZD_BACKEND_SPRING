package com.semicolon.spring.entity.feed_flag;

import com.semicolon.spring.entity.feed.Feed;
import com.semicolon.spring.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "feed_flag")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedFlag {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer flag_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


}
