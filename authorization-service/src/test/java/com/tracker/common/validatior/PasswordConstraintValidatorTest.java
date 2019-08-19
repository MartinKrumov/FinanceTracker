package com.tracker.common.validatior;

import com.tracker.rest.dto.user.UserRegisterDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class PasswordConstraintValidatorTest {

    private Validator validator;
    private UserRegisterDTO.UserRegisterDTOBuilder builder;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();

        builder = UserRegisterDTO.builder()
                .firstName("test")
                .lastName("test")
                .email("mail@mail.com")
                .username("test");
    }

    @Test
    void isValid_WithValidPassword_PassValidation() {
        //arrange
        UserRegisterDTO registerDTO = builder.password("Pass123").build();

        //act
        Set<ConstraintViolation<UserRegisterDTO>> violations = validator.validate(registerDTO);

        //assert
        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("getInvalidPasswords")
    void isValid_WithInvalidPassword_FailsValidation(String password) {
        //arrange
        UserRegisterDTO registerDTO = builder.password(password).build();

        //act
        Set<ConstraintViolation<UserRegisterDTO>> violations = validator.validate(registerDTO);

        //assert
        assertFalse(violations.isEmpty());
        violations.forEach(violation -> log.debug(violation.getMessage()));
    }

    private static Stream<String> getInvalidPasswords() {
        return Stream.of(
                "PASSWORD123", //no lower case
                "password123", //no upper case
                "Password", //no digits
                "Pa", //not enough length
                null
        );
    }
}
