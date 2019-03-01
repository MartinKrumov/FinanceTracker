package com.tracker.common.exception;

import com.tracker.common.enums.CustomEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(CustomEntity entity) {
        log.debug("{} was not found", entity);
    }
}
