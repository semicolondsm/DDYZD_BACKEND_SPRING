package com.semicolon.spring.exception;

import com.semicolon.spring.error.exception.BusinessException;
import com.semicolon.spring.error.exception.ErrorCode;

public class AlreadyPassedUserException extends BusinessException {
    public AlreadyPassedUserException(){
        super(ErrorCode.ALREADY_PASSED_USER);
    }
}
