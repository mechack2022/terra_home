package com.fragile.terra_home.exceptions;

import org.springframework.http.HttpStatus;

public class PayStackExcption extends RuntimeException {

    private HttpStatus status;
    public PayStackExcption(String message){
        super(message);
    }

    public PayStackExcption(String message, HttpStatus status){
       super(message);
       this.status = status;
    }

    public HttpStatus getStatus(){
        return status;
    }
}
