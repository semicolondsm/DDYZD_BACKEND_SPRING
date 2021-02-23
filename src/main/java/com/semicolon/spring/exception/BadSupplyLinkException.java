package com.semicolon.spring.exception;

import com.semicolon.spring.error.exception.BusinessException;
import com.semicolon.spring.error.exception.ErrorCode;

public class BadSupplyLinkException extends BusinessException {
    public BadSupplyLinkException(){
        super(ErrorCode.BAD_SUPPLY_LINK);
    }
}
