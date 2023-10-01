package com.fragile.terra_home.exceptions;

import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class ErrorDetail {

    private String error;
    private String details;
    private LocalDateTime timeStamp;
}
