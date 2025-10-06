package edu.mirea.remsely.csad.semester7.practice4.f1apiserver.controller;

import edu.mirea.remsely.csad.semester7.practice4.f1apiserver.dto.DriverInfo;
import edu.mirea.remsely.csad.semester7.practice4.f1apiserver.dto.LapTimeDto;
import edu.mirea.remsely.csad.semester7.practice4.f1apiserver.model.LapTime;
import edu.mirea.remsely.csad.semester7.practice4.f1apiserver.service.LapTimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RSocketController {
    private final LapTimeService lapTimeService;

    // Request-Response
    @MessageMapping("driver.info")
    public Mono<DriverInfo> driverInfo(Mono<String> driverNameMono) {
        return driverNameMono.flatMap(lapTimeService::findOrCreateDriver)
                .map(driver -> {
                    log.info("Returning driver info for {}", driver.getName());
                    return new DriverInfo(driver.getName(), driver.getTeam(), driver.getNumber());
                });
    }

    // Request-Stream
    @MessageMapping("lap.stream")
    public Flux<LapTimeDto> lapStream(Mono<String> driverNameMono) {
        return driverNameMono.flatMapMany(name -> {
            log.info("Starting lap stream for {}", name);
            return lapTimeService.streamSimulatedLapTimes(name, 52);
        });
    }

    // Channel
    @MessageMapping("lap.channel")
    public Flux<String> channel(Flux<String> driverNames) {
        return driverNames
                .map(String::trim)
                .flatMap(name -> lapTimeService.streamSimulatedLapTimes(name, 3)
                        .map(lv -> {
                            String msg = String.format(
                                    "update for %s: lap#%d -> %d ms",
                                    lv.getDriverName(),
                                    lv.getLapNumber(),
                                    lv.getMillis()
                            );
                            log.info(msg);
                            return msg;
                        })
                );
    }

    // Fire-and-forget
    @MessageMapping("lap.record")
    public Mono<Void> recordLap(Mono<LapTimeDto> lapViewMono) {
        return lapViewMono.flatMap(lv -> {
            LapTime lap = LapTime.builder()
                    .lapNumber(lv.getLapNumber())
                    .millis(lv.getMillis())
                    .recordedAt(lv.getRecordedAt() == null ? Instant.now() : lv.getRecordedAt())
                    .build();
            log.info("Recording lap for driver: {}", lv.getDriverName());
            return lapTimeService.saveLapTimeByDriverName(lv.getDriverName(), lap);
        }).then();
    }
}
