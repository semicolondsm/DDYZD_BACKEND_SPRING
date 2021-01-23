package com.semicolon.spring.exception;

import com.semicolon.spring.error.exception.BusinessException;
import com.semicolon.spring.error.exception.ErrorCode;

public class ClubNotFoundException extends BusinessException {
    public ClubNotFoundException(){
        super(ErrorCode.CLUB_NOT_FOUND);
    }
}
