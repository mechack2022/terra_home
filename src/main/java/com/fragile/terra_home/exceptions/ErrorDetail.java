package com.fragile.terra_home.exceptions;

import jakarta.persistence.Entity;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@RequiredArgsConstructor
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

    public ErrorDetail(String message, String description, LocalDateTime now) {
    }

    public ErrorDetail(String message, String description, HttpStatus status, LocalDateTime now) {
    }
}
