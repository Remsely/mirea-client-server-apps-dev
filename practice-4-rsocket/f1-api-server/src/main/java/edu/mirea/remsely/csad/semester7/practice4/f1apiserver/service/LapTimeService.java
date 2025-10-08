package edu.mirea.remsely.csad.semester7.practice4.f1apiserver.service;

import edu.mirea.remsely.csad.semester7.practice4.f1apiserver.dto.LapTimeDto;
import edu.mirea.remsely.csad.semester7.practice4.f1apiserver.model.Driver;
import edu.mirea.remsely.csad.semester7.practice4.f1apiserver.model.LapTime;
import edu.mirea.remsely.csad.semester7.practice4.f1apiserver.repository.DriverRepository;
import edu.mirea.remsely.csad.semester7.practice4.f1apiserver.repository.LapTimeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class LapTimeService {
    private final DriverRepository driverRepository;
    private final LapTimeRepository lapTimeRepository;

    public Mono<Driver> findOrCreateDriver(String name) {
        String trimmed = name.trim();
        return driverRepository.findByName(trimmed)
                .switchIfEmpty(
                        Mono.defer(() -> {
                            log.info("Creating new driver: {}", trimmed);
                            return driverRepository.save(Driver.builder()
                                    .name(trimmed)
                                    .team("Unknown")
                                    .number(0)
                                    .build());
                        })
                );
    }

    public Mono<LapTime> saveLapTimeForDriver(Long driverId, LapTime lap) {
        if (lap.getRecordedAt() == null) lap.setRecordedAt(Instant.now());
        lap.setDriverId(driverId);
        log.debug("Saving lap time: {}", lap);
        return lapTimeRepository.save(lap);
    }

    public Mono<LapTime> saveLapTimeByDriverName(String driverName, LapTime lap) {
        return findOrCreateDriver(driverName)
                .flatMap(driver -> saveLapTimeForDriver(driver.getId(), lap));
    }

    public Flux<LapTimeDto> streamSimulatedLapTimes(String driverName, int limit) {
        return findOrCreateDriver(driverName)
                .flatMapMany(driver ->
                        Flux.interval(Duration.ofMillis(500))
                                .take(limit)
                                .map(i -> LapTime.builder()
                                        .driverId(driver.getId())
                                        .lapNumber((int) (i + 1))
                                        .millis(ThreadLocalRandom.current().nextLong(70000, 72000))
                                        .recordedAt(Instant.now())
                                        .build()
                                )
                                .flatMap(lapTimeRepository::save)
                                .map(saved -> LapTimeDto.builder()
                                        .id(saved.getId())
                                        .driverId(saved.getDriverId())
                                        .driverName(driver.getName())
                                        .lapNumber(saved.getLapNumber())
                                        .millis(saved.getMillis())
                                        .recordedAt(saved.getRecordedAt())
                                        .build()
                                )
                                .doOnNext(v -> log.info(
                                        "[SIM] {} lap#{} -> {} ms", v.getDriverName(), v.getLapNumber(), v.getMillis())
                                )
                );
    }
}
