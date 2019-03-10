package com.tracker.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ExceptionDetails {

    private LocalDateTime dateTime;
    private String message;
    private String details;
}
