package com.semicolon.spring.security.config;

import com.semicolon.spring.security.jwt.JwtConfigure;
import com.semicolon.spring.security.jwt.JwtTokenProvider;
import com.semicolon.spring.security.jwt.RestAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    private final JwtTokenProvider jwtTokenProvider;
    private final RestAuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin().disable()
                .cors().and()
                .sessionManagement().disable()
                .csrf().disable();
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET,"/feed/list").permitAll()
                .antMatchers(HttpMethod.GET,"/feed/{club_id}/list").permitAll()
                .antMatchers(HttpMethod.GET, "/feed/{feed_id}").permitAll()
                .anyRequest().authenticated()
                .and().apply(new JwtConfigure(jwtTokenProvider));
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);

    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*");
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
