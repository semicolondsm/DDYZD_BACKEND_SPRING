package com.semicolon.spring.entity.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.semicolon.spring.entity.club.club_member.ClubMember;
import com.semicolon.spring.entity.club.club_follow.ClubFollow;
import com.semicolon.spring.entity.club.club_head.ClubHead;
import com.semicolon.spring.entity.club.room.Room;
import com.semicolon.spring.entity.feed.feed_flag.FeedFlag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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

    private String bio;

    private String session_id;

    private String mobile_session_id;

    private String desktop_session_id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JsonManagedReference
    private final Set<ClubHead> head = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JsonManagedReference
    private final Set<FeedFlag> feedFlags = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JsonManagedReference
    private final Set<ClubMember> clubMembers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JsonManagedReference
    private final Set<ClubFollow> follows = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JsonManagedReference
    private final Set<Room> rooms = new HashSet<>();

}
