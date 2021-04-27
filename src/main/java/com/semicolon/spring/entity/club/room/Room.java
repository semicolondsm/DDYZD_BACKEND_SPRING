package com.semicolon.spring.entity.club.room;

import com.semicolon.spring.entity.club.Club;
import com.semicolon.spring.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"club_id", "user_id"}))
@Entity(name = "room")
public class Room {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @Column(columnDefinition = "TINYINT(1)")
    private boolean userLooked;

    @Column(columnDefinition = "TINYINT(1)")
    private boolean clubLooked;

    @Column(columnDefinition = "INT default 0")
    private int uOffset;

    @Column(columnDefinition = "INT default 0")
    private int cOffset;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('C', 'A', 'R', 'S', 'N') default 'C'")
    private RoomStatus status;

    private LocalDateTime lastDate;

    public void setStatus(String value){
        this.status = RoomStatus.valueOf(value);
    }

}
