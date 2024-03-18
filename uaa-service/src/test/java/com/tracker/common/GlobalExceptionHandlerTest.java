package com.tracker.common;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private static final String ERRORS = "errors";

    private GlobalExceptionHandler globalExceptionHandler;
    private String errorMessage;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void globalExceptionHandlerReturnsCorrectResponse() {
        errorMessage = "TestingGlobalHandler";
        Exception exception = new Exception(errorMessage);

        //act
        ResponseEntity<ProblemDetail> result =
                globalExceptionHandler.globalExceptionHandler(exception, mock(WebRequest.class));

        //assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertNotNull(result.getBody());
        assertThat(result.getBody().getProperties()).containsEntry(ERRORS, List.of(errorMessage));
    }

    @ParameterizedTest
    @MethodSource("conflictExceptionProvider")
    void entityAlreadyExistHandlerReturnsCorrectResponse(Exception exception) {
        //act
        ResponseEntity<ProblemDetail> result =
                globalExceptionHandler.entityAlreadyExistHandler(exception, mock(WebRequest.class));

        //assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNotNull(result.getBody());
        assertThat(result.getBody().getProperties()).containsEntry(ERRORS, List.of(HttpStatus.BAD_REQUEST.name()));
    }

    @Test
    void handleMethodArgumentTypeMismatchReturnsCorrectResponse() {
        //arrange
        String parameter = "name";
        Class<String> requiredType = String.class;
        errorMessage = parameter + " should be of type " + requiredType.getSimpleName();

        MethodArgumentTypeMismatchException exception =
                new MethodArgumentTypeMismatchException("field", requiredType, parameter, mock(MethodParameter.class), new Exception("Miss match"));

        //act
        ResponseEntity<ProblemDetail> result =
                globalExceptionHandler.handleMethodArgumentTypeMismatch(exception, mock(WebRequest.class));

        //assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNotNull(result.getBody());
        assertThat(result.getBody().getProperties()).containsEntry(ERRORS, List.of(errorMessage));
    }

    @Test
    void handleMissingServletRequestPart() {
        String exceptionMessage = "Exception Message";
        MissingServletRequestPartException servletPartException = new MissingServletRequestPartException(exceptionMessage);
        HttpHeaders httpHeaders = new HttpHeaders();
        //act
        ResponseEntity<Object> result = globalExceptionHandler.handleMissingServletRequestPart(servletPartException, httpHeaders,
                HttpStatus.BAD_REQUEST, mock(WebRequest.class));

        //assert
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @ParameterizedTest
    @MethodSource("notFoundExceptionProvider")
    void entityNotFoundHandlerReturnsCorrectResponse(Exception exception) {
        //act
        ResponseEntity<ProblemDetail> result =
                globalExceptionHandler.entityNotFoundHandler(exception, mock(WebRequest.class));

        //assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNotNull(result.getBody());
        assertThat(result.getBody().getProperties()).containsEntry(ERRORS, List.of(HttpStatus.NOT_FOUND.name()));
    }

    private static Stream<Exception> conflictExceptionProvider() {
        String errorMessage = HttpStatus.BAD_REQUEST.name();
        return Stream.of(
                new IllegalStateException(errorMessage),
                new IllegalArgumentException(errorMessage)
        );
    }

    private static Stream<Exception> notFoundExceptionProvider() {
        String errorMessage = HttpStatus.NOT_FOUND.name();
        return Stream.of(
                new NoSuchElementException(errorMessage),
                new UsernameNotFoundException(errorMessage),
                new EntityNotFoundException(errorMessage)
        );
    }

}
