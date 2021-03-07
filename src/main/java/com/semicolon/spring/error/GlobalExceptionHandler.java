package com.semicolon.spring.error;

import com.semicolon.spring.error.exception.BusinessException;
import com.semicolon.spring.error.exception.ErrorCode;
import com.semicolon.spring.exception.InvalidTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.ServletException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.warn("HandleMethodArgumentNotValidException : " + e);
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.BAD_REQUEST.getStatus(), ErrorCode.BAD_REQUEST.getMessage()),
                HttpStatus.valueOf(ErrorCode.BAD_REQUEST.getStatus()));
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e){
        final ErrorCode errorCode = e.getErrorCode();
        log.warn("BusinessException : " + e);

        return new ResponseEntity<>(new ErrorResponse(errorCode.getStatus(), errorCode.getMessage()),
                HttpStatus.valueOf(errorCode.getStatus()));
    }

    @ExceptionHandler(Exception.class)
    protected  ResponseEntity<ErrorResponse> handleServletException(Exception e){
        log.warn("Exception : " + e.getStackTrace());
        return new ResponseEntity<>(new ErrorResponse(ErrorCode.INVALID_TOKEN.getStatus(), ErrorCode.INVALID_TOKEN.getMessage()),
                HttpStatus.valueOf(ErrorCode.INVALID_TOKEN.getStatus()));
    }


}
