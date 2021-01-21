package com.semicolon.spring.security.jwt;

import com.semicolon.spring.exception.InvalidTokenException;
import com.semicolon.spring.security.jwt.auth.AuthDetails;
import com.semicolon.spring.security.jwt.auth.AuthDetailsService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.prefix}")
    private String prefix;

    @Value("${jwt.header}")
    private String header;

    @Value("${jwt.exp.access}")
    private Long accessTokenExpiration;

    @Value("${jwt.exp.refresh}")
    private Long refreshTokenExpiration;

    private final AuthDetailsService authDetailsService;

    public String generateAccessToken(Integer id){
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .setSubject(id.toString())
                .claim("type", "access")
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String resolveToken(HttpServletRequest request){
        String bearer = request.getHeader(header);
        if(bearer!=null && bearer.startsWith(prefix)){
            return bearer.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token){
        try{
            Jwts.parser()
                    .setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
            return true;
        }catch (Exception e){
            throw new InvalidTokenException();
        }
    }

    public String getId(String token){
        try{
            return Jwts.parser()
                    .setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        }catch (Exception e){
            throw new InvalidTokenException();
        }
    }

    public Authentication authentication(String token){
        AuthDetails authDetails = authDetailsService.loadUserByUsername(getId(token));
        return new UsernamePasswordAuthenticationToken(authDetails, "", authDetails.getAuthorities());
    }
}
