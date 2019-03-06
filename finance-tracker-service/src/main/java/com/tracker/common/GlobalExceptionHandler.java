package com.tracker.common;

import com.tracker.common.exception.EntityAlreadyExistException;
import com.tracker.common.exception.ExceptionDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityAlreadyExistException.class)
    public ResponseEntity<ExceptionDetails> entityNotFoundException(EntityAlreadyExistException exception, WebRequest request) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, exception, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDetails> globalExceptionHandler(Exception exception, WebRequest request) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, exception, request);
    }

    private ResponseEntity<ExceptionDetails> buildResponseEntity(HttpStatus httpStatus, Exception exception, WebRequest request) {
        return ResponseEntity
                .status(httpStatus)
                .body(buildApplicationException(exception, request));
    }

    private ExceptionDetails buildApplicationException(Exception ex, WebRequest request) {
        return new ExceptionDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
    }
}
