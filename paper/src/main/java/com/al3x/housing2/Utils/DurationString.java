package com.al3x.housing2.Utils;

import java.time.Duration;
import java.time.Instant;

public class DurationString {
    public static Instant convertToExpiryTime(String durationString) {
        // Validate the input (must not be null, must be non-empty, and must contain at least one character for the value)
        if (durationString == null || durationString.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be null or empty");
        }

        // Extract numeric part and unit (e.g., "2d" -> 2 and 'd')
        String numericPart = durationString.substring(0, durationString.length() - 1);
        char unit = durationString.charAt(durationString.length() - 1);

        // Validate the numeric part is a valid integer
        int value;
        try {
            value = Integer.parseInt(numericPart);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format: " + numericPart);
        }

        if (value < 0) {
            throw new IllegalArgumentException("Duration value cannot be negative");
        }

        Duration duration = Duration.ZERO;

        // Convert based on the unit
        switch (unit) {
            case 'd':  // days
                duration = Duration.ofDays(value);
                break;
            case 'm':  // minutes
                duration = Duration.ofMinutes(value);
                break;
            case 's':  // seconds
                duration = Duration.ofSeconds(value);
                break;
            default:
                throw new IllegalArgumentException("Unsupported time unit: " + unit);
        }

        // Add duration to current timestamp
        Instant expiryTime = Instant.now().plus(duration);
        return expiryTime;
    }
}
