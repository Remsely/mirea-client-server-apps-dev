package edu.mirea.remsely.csad.semester7.practice4.f1apiserver.repository;

import edu.mirea.remsely.csad.semester7.practice4.f1apiserver.model.Driver;
import edu.mirea.remsely.csad.semester7.practice4.f1apiserver.model.LapTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.List;

@DataR2dbcTest
class LapTimeRepositoryIntegrationTest {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private LapTimeRepository lapTimeRepository;

    @Test
    void saveDriverAndLapAndQuery() {
        Driver driver = Driver.builder()
                .name("IntegrationDriver")
                .team("TeamX")
                .number(99)
                .build();

        Mono<List<LapTime>> chain = driverRepository.save(driver)
                .flatMap(savedDriver ->
                        lapTimeRepository.save(
                                LapTime.builder()
                                        .driverId(savedDriver.getId())
                                        .lapNumber(1)
                                        .millis(70123L)
                                        .recordedAt(Instant.now())
                                        .build()
                        )
                )
                .flatMap(savedLap ->
                        lapTimeRepository
                                .findAllByDriverIdOrderByLapNumberAsc(savedLap.getDriverId())
                                .collectList()
                );

        StepVerifier.create(chain)
                .expectNextMatches(list ->
                        list.size() == 1 &&
                                list.getFirst().getMillis() == 70123L &&
                                list.getFirst().getLapNumber() == 1
                )
                .verifyComplete();
    }
}
