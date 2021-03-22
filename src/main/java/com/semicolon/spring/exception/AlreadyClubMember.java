package com.semicolon.spring.exception;

import com.semicolon.spring.error.exception.BusinessException;
import com.semicolon.spring.error.exception.ErrorCode;

public class AlreadyClubMember extends BusinessException {
    public AlreadyClubMember(){
        super(ErrorCode.ALREADY_CLUB_MEMBER);
    }
}
