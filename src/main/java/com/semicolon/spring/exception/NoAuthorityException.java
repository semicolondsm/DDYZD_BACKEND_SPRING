package com.semicolon.spring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "NoAuthority")
public class NoAuthorityException extends RuntimeException{
    public NoAuthorityException(){
        super("NoAuthorityException");
    }
}
