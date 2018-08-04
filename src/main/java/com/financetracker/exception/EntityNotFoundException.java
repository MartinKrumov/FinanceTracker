package com.financetracker.exception;

import com.financetracker.enums.CustomEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(CustomEntity entitiy) {
        log.debug("{} was not found", entitiy);
    }
}
