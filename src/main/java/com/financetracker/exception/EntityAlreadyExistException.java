package com.financetracker.exception;

import com.financetracker.enums.CustomEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EntityAlreadyExistException extends RuntimeException {

    public EntityAlreadyExistException(CustomEntity entity) {
        log.debug("{} already exist.", entity.name());
    }
}