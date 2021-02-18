package com.semicolon.spring.exception;

import com.semicolon.spring.error.exception.BusinessException;
import com.semicolon.spring.error.exception.ErrorCode;

public class BadRecruitmentTimeException extends BusinessException {
    public BadRecruitmentTimeException(){
        super(ErrorCode.BAD_RECRUITMENT_TIME);
    }
}
