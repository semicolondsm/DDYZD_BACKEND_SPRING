package com.semicolon.spring.exception;

import com.semicolon.spring.error.exception.BusinessException;
import com.semicolon.spring.error.exception.ErrorCode;

public class NotClubManagerException extends BusinessException {
    public NotClubManagerException(){
        super(ErrorCode.NOT_CLUB_MANAGER);
    }
}
