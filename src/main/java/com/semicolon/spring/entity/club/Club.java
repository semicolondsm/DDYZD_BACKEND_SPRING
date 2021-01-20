package com.semicolon.spring.entity.club;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.semicolon.spring.entity.club.club_head.ClubHead;
import com.semicolon.spring.entity.club.major.Major;
import com.semicolon.spring.entity.feed.Feed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "club")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Club {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer clubId;

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

    @OneToMany(mappedBy = "club", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Feed> feeds = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "club_id")
    private ClubHead clubHead;

    @OneToMany(mappedBy = "club", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Major> majors = new HashSet<>();

    public void setClose_at(Date close_at){
        this.close_at = close_at;
    }

    public void setProfile_image(String path){
        this.profile_image = path;
    }

    public void setHongbo_image (String path){
        this.hongbo_image = path;
    }

    public void setBanner_image(String path){
        this.banner_image = path;
    }

    public void setClub_name(String name){
        this.club_name = name;
    }

}

