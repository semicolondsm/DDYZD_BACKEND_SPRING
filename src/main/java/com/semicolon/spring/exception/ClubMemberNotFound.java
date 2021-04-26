package com.semicolon.spring.exception;

import com.semicolon.spring.error.exception.BusinessException;
import com.semicolon.spring.error.exception.ErrorCode;

public class ClubMemberNotFound extends BusinessException {
    public ClubMemberNotFound(){
        super(ErrorCode.CLUB_MEMBER_NOT_FOUND);
    }
}
