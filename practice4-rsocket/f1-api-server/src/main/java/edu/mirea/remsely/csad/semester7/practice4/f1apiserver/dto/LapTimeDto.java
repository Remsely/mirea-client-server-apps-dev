package edu.mirea.remsely.csad.semester7.practice4.f1apiserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LapTimeDto {
    private Long id;
    private Long driverId;
    private String driverName;
    private int lapNumber;
    private long millis;
    private Instant recordedAt;
}
