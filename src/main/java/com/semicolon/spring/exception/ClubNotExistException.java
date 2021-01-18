package com.semicolon.spring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "ClubNotExist")
public class ClubNotExistException extends RuntimeException{
    public ClubNotExistException(){
        super("ClubNotExistException");
    }
}
