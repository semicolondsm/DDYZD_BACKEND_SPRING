package com.semicolon.spring.exception;

import com.semicolon.spring.error.exception.BusinessException;
import com.semicolon.spring.error.exception.ErrorCode;

public class NoAuthorityException extends BusinessException {
    public NoAuthorityException(){
        super(ErrorCode.NO_AUTHORITY);
    }
}
