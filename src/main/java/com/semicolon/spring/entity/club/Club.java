package com.semicolon.spring.entity.club;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.semicolon.spring.entity.club.club_member.ClubMember;
import com.semicolon.spring.entity.club.club_follow.ClubFollow;
import com.semicolon.spring.entity.club.club_head.ClubHead;
import com.semicolon.spring.entity.club.major.Major;
import com.semicolon.spring.entity.club.room.Room;
import com.semicolon.spring.entity.feed.Feed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "club")
public class Club {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer clubId;

    @Column(nullable = false, length = 45)
    private String name;

    @Column(nullable = false)
    private int totalBudget;

    @Column(nullable = false)
    private int currentBudget;

    private Date startAt;

    private Date closeAt;

    @Column(nullable = false)
    private String bannerImage;

    @Column(nullable = false)
    private String profile_image;

    @Column(nullable = false)
    private String hongboImage;

    private String description;

    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY)
    @JsonManagedReference
    private final Set<Feed> feeds = new HashSet<>();

    @OneToOne(mappedBy = "club", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private ClubHead clubHead;

    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Major> majors = new HashSet<>();

    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY)
    @JsonManagedReference
    private final Set<ClubMember> clubMembers = new HashSet<>();

    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY)
    @JsonManagedReference
    private final Set<ClubFollow> follows = new HashSet<>();

    @OneToMany(mappedBy = "club", fetch = FetchType.LAZY)
    @JsonManagedReference
    private final Set<Room> rooms = new HashSet<>();

    public void setCloseAt(Date close_at){
        this.closeAt = close_at;
    }

    public void setProfile_image(String path){
        this.profile_image = path;
    }

    public void setHongboImage(String path){
        this.hongboImage = path;
    }

    public void setBannerImage(String path){
        this.bannerImage = path;
    }

    public void setClub_name(String name){
        this.name = name;
    }

    public void setStart_at(){
        this.startAt = new Date();
    }

    public void setStartAt(Date date){
        this.startAt = date;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setMajors(){
        this.majors = new HashSet<>();
    }

}

