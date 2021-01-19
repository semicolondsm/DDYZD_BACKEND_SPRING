package com.semicolon.spring.security.jwt.auth;

import com.semicolon.spring.entity.user.UserRepository;
import com.semicolon.spring.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public AuthDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        return userRepository.findById(Integer.parseInt(id))
                .map(AuthDetails::new)
                .orElseThrow(UserNotFoundException::new);
    }
}
