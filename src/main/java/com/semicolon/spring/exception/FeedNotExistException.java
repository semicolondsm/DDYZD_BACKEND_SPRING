package com.semicolon.spring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "FeedNotExist")
public class FeedNotExistException extends RuntimeException{
    public FeedNotExistException(){
        super("FeedNotExistException");
    }
}
