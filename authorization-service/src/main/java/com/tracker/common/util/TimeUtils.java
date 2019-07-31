package com.tracker.common.util;

import java.time.Duration;
import java.time.Instant;

/**
 * Time utils.
 *
 * @author Martin Krumov
 */
public final class TimeUtils {

    private TimeUtils() {
        throw new IllegalStateException("This is utility class.");
    }

    /**
     * Checks if given {@link Instant} is before {@link Instant#now()} given number of secounds
     *
     * @param instant the instant
     * @param seconds    the number of secounds
     * @return true if the instant is after, false otherwise
     */
    public static boolean isBefore(Instant instant, Long seconds) {
        return instant.isBefore(Instant.now().plusSeconds(seconds));
    }
}
