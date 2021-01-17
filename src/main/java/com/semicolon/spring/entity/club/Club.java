package com.semicolon.spring.entity.club;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.semicolon.spring.entity.feed.Feed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity(name = "club")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Club {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer club_id;

    @Column(nullable = false, length = 45)
    private String club_name;

    @Column(nullable = false)
    private int total_budget;

    @Column(nullable = false)
    private int current_budget;

    private Date start_at;

    private Date close_at;

    @Column(nullable = false)
    private String banner_image;

    @Column(nullable = false)
    private String profile_image;

    @Column(nullable = false)
    private String hongbo_image;

    @OneToMany(mappedBy = "club_id", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Feed> feeds;

}

