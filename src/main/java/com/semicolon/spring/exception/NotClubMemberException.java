package com.semicolon.spring.exception;

import com.semicolon.spring.error.exception.BusinessException;
import com.semicolon.spring.error.exception.ErrorCode;

public class NotClubMemberException extends BusinessException {
    public NotClubMemberException(){
        super(ErrorCode.NOT_CLUB_MEMBER);
    }
}
