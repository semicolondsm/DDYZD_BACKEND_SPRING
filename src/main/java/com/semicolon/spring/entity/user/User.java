package com.semicolon.spring.entity.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.semicolon.spring.entity.club.application.Application;
import com.semicolon.spring.entity.club.club_follow.ClubFollow;
import com.semicolon.spring.entity.club.club_head.ClubHead;
import com.semicolon.spring.entity.feed.feed_flag.FeedFlag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer user_id;

    @Column(length = 15)
    private String name;

    @Column(length = 5)
    private String gcn;

    private String image_path;

    private String github_url;

    @Column(length = 50)
    private String email;

    @Length(max = 4096)
    private String device_token;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    @JsonManagedReference
    private Set<ClubHead> head = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    @JsonManagedReference
    private Set<FeedFlag> feedFlags = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    @JsonManagedReference
    private Set<Application> applications = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    @JsonManagedReference
    private Set<ClubFollow> follows = new HashSet<>();
}
