package com.semicolon.spring.entity.club.club_member;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.semicolon.spring.entity.club.Club;
import com.semicolon.spring.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "club_member")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClubMember {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    @JsonBackReference
    private Club club;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    public int getId(){
        return this.id;
    }
}
