package com.semicolon.spring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "FileNotSave")
public class FileNotSaveException extends RuntimeException{
    public FileNotSaveException(){
        super("FileNotSaveException");
    }
}
