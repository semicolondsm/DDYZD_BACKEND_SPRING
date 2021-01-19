package com.semicolon.spring.entity.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.semicolon.spring.entity.club.club_head.ClubHead;
import com.semicolon.spring.entity.feed.feed_flag.FeedFlag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.List;

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

    @Column(length = 45)
    private String image_path;

    @Column(length = 45)
    private String github_url;

    @Column(length = 50)
    private String email;

    @Length(max = 4096)
    private String device_token;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user_id")
    @JsonManagedReference
    private List<ClubHead> head;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "club_id")
    @JsonManagedReference
    private List<FeedFlag> feedFlags;
}
