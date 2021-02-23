package com.semicolon.spring.exception;

import com.semicolon.spring.error.exception.BusinessException;
import com.semicolon.spring.error.exception.ErrorCode;

public class ApplicationNotFoundException extends BusinessException {
    public ApplicationNotFoundException(){
        super(ErrorCode.APPLICATION_NOT_FOUND);
    }
}
