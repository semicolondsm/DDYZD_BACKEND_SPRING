package com.semicolon.spring.exception;

import com.semicolon.spring.error.exception.BusinessException;
import com.semicolon.spring.error.exception.ErrorCode;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException(){
        super(ErrorCode.USER_NOT_FOUND);
    }
}
