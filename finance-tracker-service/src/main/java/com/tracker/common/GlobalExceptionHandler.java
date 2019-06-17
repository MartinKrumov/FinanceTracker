package com.tracker.common;

import com.tracker.common.exception.EntityAlreadyExistException;
import com.tracker.common.exception.ExceptionDetails;
import com.tracker.common.exception.WalletNameAlreadyExists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

/**
 * GlobalExceptionHandler
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Default exception handler for {@link Exception}
     *
     * @param exception {@link Throwable}
     * @param request   {@link WebRequest}
     * @return {@link ResponseEntity} with {@link HttpStatus#INTERNAL_SERVER_ERROR} and {@link ExceptionDetails}
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ExceptionDetails> globalExceptionHandler(Throwable exception, WebRequest request) {
        log.error(exception.getMessage(), exception);
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, exception, request);
    }

    /**
     * Default exception handler for {@link EntityAlreadyExistException}, {@link WalletNameAlreadyExists}
     *
     * @param exception {@link EntityAlreadyExistException}
     * @param request   {@link WebRequest}
     * @return {@link ResponseEntity} with {@link HttpStatus#CONFLICT} and {@link ExceptionDetails}
     */
    @ExceptionHandler({EntityAlreadyExistException.class, WalletNameAlreadyExists.class})
    public ResponseEntity<ExceptionDetails> entityAlreadyExistHandler(Exception exception, WebRequest request) {
        log.error(exception.getMessage(), exception);
        return buildResponseEntity(HttpStatus.CONFLICT, exception, request);
    }

    /**
     * Default exception handler for handling NOT_FOUND
     *
     * @param exception {@link Exception}
     * @param request   {@link WebRequest}
     * @return {@link ResponseEntity} with {@link HttpStatus#NOT_FOUND} and {@link ExceptionDetails}
     */
    @ExceptionHandler({UsernameNotFoundException.class, NoSuchElementException.class})
    public ResponseEntity<ExceptionDetails> entityNotFoundHandler(Exception exception, WebRequest request) {
        log.error(exception.getMessage(), exception);
        return buildResponseEntity(HttpStatus.NOT_FOUND, exception, request);
    }

    /**
     * Builds ResponseEntity with given {@link HttpStatus} and {@link ExceptionDetails}.
     * See also {@link #buildApplicationException(Throwable, WebRequest)}.
     *
     * @param httpStatus {@link HttpStatus}
     * @param exception  {@link Throwable}
     * @param request    {@link WebRequest}
     * @return {@link ResponseEntity}
     */
    private ResponseEntity<ExceptionDetails> buildResponseEntity(HttpStatus httpStatus, Throwable exception, WebRequest request) {
        return ResponseEntity
                .status(httpStatus)
                .body(buildApplicationException(exception, request));
    }

    /**
     * Build exception details.
     *
     * @param ex  {@link Throwable}
     * @param request    {@link WebRequest}
     * @return the exception details
     */
    private ExceptionDetails buildApplicationException(Throwable ex, WebRequest request) {
        return ExceptionDetails.builder()
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .details(request.getDescription(false))
                .build();
    }
}
