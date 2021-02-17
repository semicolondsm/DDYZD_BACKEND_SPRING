package com.semicolon.spring.exception;

import com.semicolon.spring.error.exception.BusinessException;
import com.semicolon.spring.error.exception.ErrorCode;

public class BadFileExtensionException extends BusinessException {
    public BadFileExtensionException(){
        super(ErrorCode.BAD_FILE_EXTENSION);
    }
}
