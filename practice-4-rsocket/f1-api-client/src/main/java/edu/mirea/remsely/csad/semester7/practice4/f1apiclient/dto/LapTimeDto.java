package edu.mirea.remsely.csad.semester7.practice4.f1apiclient.dto;

import java.time.Instant;

public record LapTimeDto(
        Long id,
        Long driverId,
        String driverName,
        int lapNumber,
        long millis,
        Instant recordedAt
) {
}
