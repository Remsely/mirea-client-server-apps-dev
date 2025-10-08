package edu.mirea.remsely.csad.semester7.practice4.f1apiserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("lap_times")
public class LapTime {
    @Id
    private Long id;

    private Long driverId;

    private int lapNumber;

    private long millis;

    private Instant recordedAt;
}
