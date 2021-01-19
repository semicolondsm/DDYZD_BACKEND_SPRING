package com.semicolon.spring.entity.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Entity(name = "user")
@AllArgsConstructor
@NoArgsConstructor
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
}
