package com.semicolon.spring.exception;

import com.semicolon.spring.error.exception.BusinessException;
import com.semicolon.spring.error.exception.ErrorCode;

public class ClubManagerNotFound extends BusinessException {
    public ClubManagerNotFound(){
        super(ErrorCode.CLUB_MANAGER_NOT_FOUND);
    }
}
