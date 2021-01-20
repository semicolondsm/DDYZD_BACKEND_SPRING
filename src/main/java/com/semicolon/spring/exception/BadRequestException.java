package com.semicolon.spring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "BadRequest")
public class BadRequestException extends RuntimeException{
    public BadRequestException(){
        super("BadRequestException");
    }
}
