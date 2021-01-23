package com.semicolon.spring.exception;

import com.semicolon.spring.error.exception.BusinessException;
import com.semicolon.spring.error.exception.ErrorCode;

public class FileSaveFailException extends BusinessException {
    public FileSaveFailException(){
        super(ErrorCode.FILE_SAVE_FAIL);
    }
}
