package com.tracker.common.exception;

import com.tracker.common.enums.CustomEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EntityAlreadyExistException extends RuntimeException {

    public EntityAlreadyExistException(CustomEntity entity) {
        super();
        log.info("{} already exist.", entity.name());
    }

    public EntityAlreadyExistException(String message) {
        super(message);
    }
}