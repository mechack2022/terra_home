package com.fragile.terra_home.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class ResourceNotFoundException extends RuntimeException {

    private HttpStatus status;
    public ResourceNotFoundException(String message){
        super(message);
    }
    public ResourceNotFoundException(String message, HttpStatus status){
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus(){
        return status;
    }


}
