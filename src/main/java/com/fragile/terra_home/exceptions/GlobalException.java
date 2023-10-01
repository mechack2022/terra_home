package com.fragile.terra_home.exceptions;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorDetail>  handleUserException(UserException userEx, WebRequest req){
        ErrorDetail errorDetail = new ErrorDetail(userEx.getMessage(), req.getDescription(false), LocalDateTime.now() );
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<ErrorDetail> handleConstraintViolationException(ConstraintViolationException ex) {
//        StringBuilder errorMessages = new StringBuilder();
//
//        for (ConstraintViolation<?> violation :ex.getConstraintViolations()) {
//            errorMessages.append(violation.getMessage()).append("\n");
//        }
//
//        ErrorDetail errorDetail = new ErrorDetail(errorMessages.toString(), "violation error", LocalDateTime.now());
//        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetail> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        ErrorDetail errorDetail = new ErrorDetail(Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage(),
                "validation error", LocalDateTime.now());
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDetail> handleDataIntegrityViolationException(DataIntegrityViolationException dEx){
        String message =  Objects.requireNonNull(dEx.getRootCause()).getMessage();
        String duplicateValue = "";
        String[] tokens = message.split(" ");
        duplicateValue = tokens[2] + " Already use with another account";
        ErrorDetail errorDetail = new ErrorDetail(duplicateValue, " Encounter duplicate", LocalDateTime.now());
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetail> handleOtherExceptions(Exception ex, WebRequest req){
        ErrorDetail errorDetail = new ErrorDetail(ex.getMessage(), req.getDescription(false), LocalDateTime.now() );
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }
}
