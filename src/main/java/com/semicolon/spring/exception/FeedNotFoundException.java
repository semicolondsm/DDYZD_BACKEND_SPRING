package com.semicolon.spring.exception;

import com.semicolon.spring.error.exception.BusinessException;
import com.semicolon.spring.error.exception.ErrorCode;

public class FeedNotFoundException extends BusinessException {
    public FeedNotFoundException(){
        super(ErrorCode.FEED_NOT_FOUND);
    }
}
