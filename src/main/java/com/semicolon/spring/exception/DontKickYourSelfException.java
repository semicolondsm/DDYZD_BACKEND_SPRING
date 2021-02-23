package com.semicolon.spring.exception;

import com.semicolon.spring.error.exception.BusinessException;
import com.semicolon.spring.error.exception.ErrorCode;

public class DontKickYourSelfException extends BusinessException {
    public DontKickYourSelfException(){
        super(ErrorCode.DONT_KICK_YOUR_SELF);
    }
}
