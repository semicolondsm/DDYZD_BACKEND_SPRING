package com.semicolon.spring.exception;

import com.semicolon.spring.error.exception.BusinessException;
import com.semicolon.spring.error.exception.ErrorCode;

public class AlreadyClubManagerException extends BusinessException {
    public AlreadyClubManagerException(){
        super(ErrorCode.ALREADY_CLUB_MANAGER);
    }
}
