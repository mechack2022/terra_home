package com.fragile.terra_home.exceptions;

import org.springframework.http.HttpStatus;

public class UserException extends RuntimeException {


    public UserException(String message) {
        super(message);
    }

}
