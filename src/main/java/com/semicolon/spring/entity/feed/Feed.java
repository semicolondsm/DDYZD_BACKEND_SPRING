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
import java.util.List;

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

    @OneToMany(mappedBy = "feed_id", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<FeedMedium> media;

    @CreationTimestamp
    private Date uploadAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    @JsonBackReference
    private Club clubId;

    private int flag;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "feed_id")
    @JsonManagedReference
    private List<FeedFlag> feedFlags;

    public void modify(String contents, boolean isPin){
        this.contents = contents;
        this.pin = isPin;
    }

    public void addFlag(){
        flag++;
    }

    public void deleteFlag(){
        flag--;
    }
}
