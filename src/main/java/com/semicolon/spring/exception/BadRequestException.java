package com.semicolon.spring.exception;

import com.semicolon.spring.error.exception.BusinessException;
import com.semicolon.spring.error.exception.ErrorCode;

public class BadRequestException extends BusinessException {
    public BadRequestException(){
        super(ErrorCode.BAD_REQUEST);
    }
}
