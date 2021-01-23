package com.semicolon.spring.exception;

import com.semicolon.spring.error.exception.BusinessException;
import com.semicolon.spring.error.exception.ErrorCode;

public class NotClubHeadException extends BusinessException {
    public NotClubHeadException(){
        super(ErrorCode.NOT_CLUB_HEAD);
    }
}
