package com.semicolon.spring.exception;

import com.semicolon.spring.error.exception.BusinessException;
import com.semicolon.spring.error.exception.ErrorCode;

public class InvalidTokenException extends BusinessException {
    public InvalidTokenException(){
        super(ErrorCode.INVALID_TOKEN);
    }
}
