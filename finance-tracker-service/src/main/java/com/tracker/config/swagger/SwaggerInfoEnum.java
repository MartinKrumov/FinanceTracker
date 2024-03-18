package com.tracker.config.swagger;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SwaggerInfoEnum {

    BASE_PACKAGES(Value.BASE_PACKAGES),
    TITLE(Value.TITLE),
    DESCRIPTION(Value.DESCRIPTION),
    LICENSE(Value.LICENSE),
    VERSION(Value.VERSION);

    @Getter
    private final String value;

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    static class Value {

        static final String BASE_PACKAGES = "com.tracker.rest";
        static final String TITLE = "Finance Tracker Service REST API";
        static final String DESCRIPTION = "Finance Tracker Service";
        static final String LICENSE = "Apache License Version 2.0";
        static final String LICENSE_URL = "http://www.apache.org/licenses/LICENSE-2.0";
        static final String VERSION = "1.0.0";
    }
}
