package com.semicolon.spring.exception;

import com.semicolon.spring.error.exception.BusinessException;
import com.semicolon.spring.error.exception.ErrorCode;

public class FileNotFoundException extends BusinessException {
    public FileNotFoundException(){
        super(ErrorCode.FILE_NOT_FOUND);
    }
}
