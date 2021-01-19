package com.semicolon.spring.security;

import com.semicolon.spring.entity.user.User;
import com.semicolon.spring.security.jwt.auth.AuthDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {
    public Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public User getUser(){
        return ((AuthDetails)this.getAuthentication().getPrincipal()).getUser();
    }
}
