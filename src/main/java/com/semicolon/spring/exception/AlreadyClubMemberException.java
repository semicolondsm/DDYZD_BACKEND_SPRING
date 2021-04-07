package com.semicolon.spring.exception;

import com.semicolon.spring.error.exception.BusinessException;
import com.semicolon.spring.error.exception.ErrorCode;

public class AlreadyClubMemberException extends BusinessException {
    public AlreadyClubMemberException(){
        super(ErrorCode.ALREADY_CLUB_MEMBER);
    }
}
