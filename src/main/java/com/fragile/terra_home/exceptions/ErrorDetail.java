package com.fragile.terra_home.exceptions;

import jakarta.persistence.Entity;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class ErrorDetail {

    private String error;
    private String details;

    private HttpStatus status;
    private LocalDateTime timeStamp;

    public ErrorDetail(String error, String details, LocalDateTime timeStamp) {
        this.error = error;
        this.details = details;
        this.timeStamp = timeStamp;
    }


    public ErrorDetail(String error, String details, HttpStatus status, LocalDateTime timeStamp) {
        this.error = error;
        this.details = details;
        this.status = status;
        this.timeStamp = timeStamp;
    }

}
