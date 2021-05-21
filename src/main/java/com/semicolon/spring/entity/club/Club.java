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
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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

    private LocalDateTime startAt;

    private Date closeAt;

    @Column(nullable = false)
    private String bannerImage;

    @Column(nullable = false)
    private String profileImage;

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

    public void setCloseAt(Date closeAt){
        this.closeAt = closeAt;
    }

    public Club setProfileImage(String path){
        this.profileImage = path;
        return this;
    }

    public Club setHongboImage(String path){
        this.hongboImage = path;
        return this;
    }

    public Club setBannerImage(String path){
        this.bannerImage = path;
        return this;
    }

    public Club setClubName(String name){
        this.name = name;
        return this;
    }

    public void setStartAt(){
        this.startAt = LocalDateTime.now(ZoneOffset.of("+9"));
    }

    public void setStartAt(LocalDateTime date){
        this.startAt = date;
    }

    public Club setDescription(String description){
        this.description = description;
        return this;
    }

    public void setMajors(){
        this.majors = new HashSet<>();
    }

}

