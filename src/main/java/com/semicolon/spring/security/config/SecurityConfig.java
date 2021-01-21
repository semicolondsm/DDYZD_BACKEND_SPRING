package com.semicolon.spring.security.config;

import com.semicolon.spring.security.jwt.JwtConfigure;
import com.semicolon.spring.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin().disable()
                .cors().disable()
                .csrf().disable();
        http.authorizeRequests()
                .antMatchers("/feed/list").permitAll()
                .antMatchers("/feed/{club_id}/list").permitAll()
                .anyRequest().authenticated()
                .and().apply(new JwtConfigure(jwtTokenProvider));

    }
}
