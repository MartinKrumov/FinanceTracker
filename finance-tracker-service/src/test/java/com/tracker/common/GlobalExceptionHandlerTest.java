package com.tracker.common;

import com.tracker.common.exception.ApiError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * @author Martin Krumov
 */
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void globalExceptionHandlerBuildProperResponse() {
        String errorMessage = "TestingGlobalHandler";
        Exception exception = new Exception(errorMessage);

        //act
        ResponseEntity<ApiError> result =
                globalExceptionHandler.globalExceptionHandler(exception, mock(WebRequest.class));

        //assert
        assertEquals(result.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        assertNotNull(result.getBody());
        assertThat(result.getBody().getErrorMessages(), hasItem(errorMessage));
    }
}
