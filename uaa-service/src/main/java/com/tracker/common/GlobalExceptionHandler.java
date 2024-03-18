package com.tracker.common;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

/**
 * GlobalExceptionHandler
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Default exception handler for {@link Exception}
     *
     * @param ex      {@link Throwable}
     * @param request {@link WebRequest}
     * @return {@link ResponseEntity} with {@link HttpStatus#INTERNAL_SERVER_ERROR} and {@link ProblemDetail}
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ProblemDetail> globalExceptionHandler(Throwable ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, List.of(ex.getLocalizedMessage()));
    }

    /**
     * Default exception handler for {@link IllegalArgumentException}, {@link IllegalStateException}
     *
     * @param ex      {@link Exception}
     * @param request {@link WebRequest}
     * @return {@link ResponseEntity} with {@link HttpStatus#BAD_REQUEST} and {@link ProblemDetail}
     */
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ProblemDetail> entityAlreadyExistHandler(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return buildResponseEntity(HttpStatus.BAD_REQUEST, List.of(ex.getLocalizedMessage()));
    }

    /**
     * Default ex handler for handling NOT_FOUND
     *
     * @param ex      {@link Exception}
     * @param request {@link WebRequest}
     * @return {@link ResponseEntity} with {@link HttpStatus#NOT_FOUND} and {@link ProblemDetail}
     */
    @ExceptionHandler({UsernameNotFoundException.class, NoSuchElementException.class, EntityNotFoundException.class})
    public ResponseEntity<ProblemDetail> entityNotFoundHandler(Exception ex, WebRequest request) {
        log.warn(ex.getMessage(), ex);
        return buildResponseEntity(HttpStatus.NOT_FOUND, List.of(ex.getLocalizedMessage()));
    }

    //400
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ProblemDetail> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String requiredType = Optional.ofNullable(ex.getRequiredType())
                .map(clazz -> ex.getRequiredType().getSimpleName())
                .orElse("");

        String error = ex.getName() + " should be of type " + requiredType;
        ProblemDetail problemDetail = buildProblemDetailWithDetail(HttpStatus.BAD_REQUEST, error, List.of(error));

        return buildResponseEntity(HttpStatus.BAD_REQUEST, problemDetail);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ProblemDetail> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        logger.info(ex.getClass().getName());

        List<String> errors = ex.getConstraintViolations().stream()
                .map(violation -> violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": " + violation.getMessage())
                .toList();

        return buildResponseEntity(HttpStatus.BAD_REQUEST, errors);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
                                                        HttpStatusCode status, WebRequest request) {
        logger.info(ex.getClass().getName());

        String error = ex.getValue() + " value for " + ex.getPropertyName() + " should be of type " + ex.getRequiredType();
        ProblemDetail problemDetail = buildProblemDetailWithDetail(HttpStatus.BAD_REQUEST, error, List.of(error));

        return handleExceptionInternal(ex, problemDetail, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex,
                                                                     HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        logger.info(ex.getClass().getName());

        String error = ex.getRequestPartName() + " part is missing";
        ProblemDetail problemDetail = fillProblemDetail(ex.getBody(), List.of(error));

        return handleExceptionInternal(ex, problemDetail, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        logger.info(ex.getClass().getName());

        String error = ex.getParameterName() + " parameter is missing";
        ProblemDetail problemDetail = fillProblemDetail(ex.getBody(), List.of(error));

        return handleExceptionInternal(ex, problemDetail, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        logger.info(ex.getClass().getName());
        List<String> errors = extractErrorMessages(ex.getBindingResult());

        ProblemDetail problemDetail = fillProblemDetail(ex.getBody(), errors);

        return handleExceptionInternal(ex, problemDetail, headers, HttpStatus.BAD_REQUEST, request);
    }

    // 404
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        logger.info(ex.getClass().getName());
        String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();

        ProblemDetail problemDetail = fillProblemDetail(ex.getBody(), List.of(error));

        return handleExceptionInternal(ex, problemDetail, headers, HttpStatus.NOT_FOUND, request);
    }

    // 405
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        logger.info(ex.getClass().getName());

        String detail = ex.getMethod() + " method is not supported for this request. Supported methods are: ";
        StringBuilder sb = new StringBuilder(detail);

        Optional.ofNullable(ex.getSupportedHttpMethods())
                .ifPresent(httpMethods -> httpMethods.forEach(t -> sb.append(t).append(" ")));

        ProblemDetail problemDetail = buildProblemDetailWithDetail(HttpStatus.METHOD_NOT_ALLOWED, detail, List.of(sb.toString()));

        return handleExceptionInternal(ex, problemDetail, headers, HttpStatus.METHOD_NOT_ALLOWED, request);
    }

    // 415
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                     HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        logger.info(ex.getClass().getName());

        String error = ex.getContentType() + " media type is not supported. Supported media types are ";
        StringBuilder sb = new StringBuilder(error);

        ex.getSupportedMediaTypes()
                .forEach(t -> sb.append(t).append(" "));

        ProblemDetail problemDetail = buildProblemDetailWithDetail(HttpStatus.UNSUPPORTED_MEDIA_TYPE, error,
                List.of(sb.substring(0, sb.length() - 2)));

        return handleExceptionInternal(ex, problemDetail, headers, HttpStatus.UNSUPPORTED_MEDIA_TYPE, request);
    }

    /**
     * Get field and global errors from bindingResult
     *
     * @param bindingResult {@link BindingResult}
     * @return list of error messages
     */
    private List<String> extractErrorMessages(BindingResult bindingResult) {

        List<String> errors = bindingResult.getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(toList());

        bindingResult.getGlobalErrors().forEach(error ->
                errors.add(error.getObjectName() + ": " + error.getDefaultMessage()));

        return errors;
    }

    /**
     * Builds ResponseEntity with given {@link HttpStatus} and {@link ProblemDetail}.
     *
     * @param httpStatus    {@link HttpStatus}
     * @param errorMessages errors
     * @return {@link ResponseEntity}
     */
    private ResponseEntity<ProblemDetail> buildResponseEntity(HttpStatus httpStatus, List<String> errorMessages) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(httpStatus);
        fillProblemDetail(problemDetail, errorMessages);
        return buildResponseEntity(httpStatus, problemDetail);
    }

    /**
     * Builds ResponseEntity with given {@link HttpStatus} and {@link ProblemDetail}.
     *
     * @param httpStatus    {@link HttpStatus}
     * @param problemDetail {@link ProblemDetail}
     * @return {@link ResponseEntity}
     */
    private ResponseEntity<ProblemDetail> buildResponseEntity(HttpStatus httpStatus, ProblemDetail problemDetail) {
        return ResponseEntity.status(httpStatus)
                .body(problemDetail);
    }

    private ProblemDetail buildProblemDetailWithDetail(HttpStatus status, String detail, List<String> errorMessages) {
        ProblemDetail problemDetail = StringUtils.isNotBlank(detail) ? ProblemDetail.forStatusAndDetail(status, detail)
                : ProblemDetail.forStatus(status);

        return fillProblemDetail(problemDetail, errorMessages);
    }

    private ProblemDetail fillProblemDetail(ProblemDetail problemDetail, List<String> errors) {
        problemDetail.setProperty("timestamp", Instant.now());
        if (isNotEmpty(errors)) {
            problemDetail.setProperty("errors", errors);
        }

        return problemDetail;
    }

}
