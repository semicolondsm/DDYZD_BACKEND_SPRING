package com.semicolon.spring.exception;

import com.semicolon.spring.error.exception.BusinessException;
import com.semicolon.spring.error.exception.ErrorCode;

public class NotFoundException extends BusinessException {
    public NotFoundException(){
        super(ErrorCode.NOT_FOUND);
    }
}
