package com.semicolon.spring.entity.feed;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.semicolon.spring.entity.club.Club;
import com.semicolon.spring.entity.feed.feed_flag.FeedFlag;
import com.semicolon.spring.entity.feed.feed_medium.FeedMedium;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "feed")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Feed {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String contents;

    private boolean pin;

    @OneToMany(mappedBy = "feed", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private Set<FeedMedium> media = new HashSet<>();

    @CreationTimestamp
    private Date uploadAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    @JsonBackReference
    private Club club;


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "feed", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private Set<FeedFlag> feedFlags = new HashSet<>();

    public void modify(String contents, boolean isPin){
        this.contents = contents;
        this.pin = isPin;
    }

    public void changePin(){
        this.pin = !this.pin;
    }

}
