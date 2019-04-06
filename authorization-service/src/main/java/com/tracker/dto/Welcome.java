package com.tracker.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class Welcome implements Serializable {

    private static final String GREETINGS_FORMAT = "Welcome %s!";

    public final String greetings;

    public Welcome(String who) {
        this.greetings = String.format(GREETINGS_FORMAT, who);
    }
}
