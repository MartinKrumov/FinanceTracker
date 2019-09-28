package com.tracker.common;

import com.tracker.common.exception.ApiError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

/**
 * @author Martin Krumov
 */
class GlobalExceptionHandlerTest {

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
        ResponseEntity<ApiError> result =
                globalExceptionHandler.globalExceptionHandler(exception, mock(WebRequest.class));

        //assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertNotNull(result.getBody());
        assertThat(result.getBody().getErrorMessages(), hasItem(errorMessage));
    }

    @ParameterizedTest
    @MethodSource("conflictExceptionProvider")
    void entityAlreadyExistHandlerReturnsCorrectResponse(Exception exception) {
        //act
        ResponseEntity<ApiError> result =
                globalExceptionHandler.entityAlreadyExistHandler(exception, mock(WebRequest.class));

        //assert
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertNotNull(result.getBody());
        assertThat(result.getBody().getErrorMessages(), hasItem(HttpStatus.CONFLICT.name()));
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
        ResponseEntity<ApiError> result =
                globalExceptionHandler.handleMethodArgumentTypeMismatch(exception, mock(WebRequest.class));

        //assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNotNull(result.getBody());
        assertThat(result.getBody().getErrorMessages(), hasItem(errorMessage));
    }

    @ParameterizedTest
    @MethodSource("notFoundExceptionProvider")
    void entityNotFoundHandlerReturnsCorrectResponse(Exception exception) {
        //act
        ResponseEntity<ApiError> result =
                globalExceptionHandler.entityNotFoundHandler(exception, mock(WebRequest.class));

        //assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNotNull(result.getBody());
        assertThat(result.getBody().getErrorMessages(), hasItem(HttpStatus.NOT_FOUND.name()));
    }

    private static Stream<Exception> conflictExceptionProvider() {
        String errorMessage = HttpStatus.CONFLICT.name();
        return Stream.of(
                new IllegalStateException(errorMessage),
                new IllegalArgumentException(errorMessage)
        );
    }

    private static Stream<Exception> notFoundExceptionProvider() {
        String errorMessage = HttpStatus.NOT_FOUND.name();
        return Stream.of(
                new NoSuchElementException(errorMessage),
                new UsernameNotFoundException(errorMessage)
        );
    }

}
