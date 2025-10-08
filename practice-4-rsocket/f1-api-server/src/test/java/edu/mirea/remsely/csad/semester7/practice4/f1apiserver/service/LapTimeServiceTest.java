package edu.mirea.remsely.csad.semester7.practice4.f1apiserver.service;

import edu.mirea.remsely.csad.semester7.practice4.f1apiserver.model.Driver;
import edu.mirea.remsely.csad.semester7.practice4.f1apiserver.model.LapTime;
import edu.mirea.remsely.csad.semester7.practice4.f1apiserver.repository.DriverRepository;
import edu.mirea.remsely.csad.semester7.practice4.f1apiserver.repository.LapTimeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LapTimeServiceTest {

    private DriverRepository driverRepository;
    private LapTimeRepository lapTimeRepository;
    private LapTimeService svc;

    @BeforeEach
    void setup() {
        driverRepository = mock(DriverRepository.class);
        lapTimeRepository = mock(LapTimeRepository.class);
        svc = new LapTimeService(driverRepository, lapTimeRepository);
    }

    @Test
    void findOrCreateDriver_creates_when_missing() {
        when(driverRepository.findByName("NewDriver"))
                .thenReturn(Mono.empty());

        when(driverRepository.save(any(Driver.class)))
                .thenReturn(
                        Mono.just(
                                Driver.builder()
                                        .id(1L)
                                        .name("NewDriver")
                                        .team("Unknown")
                                        .number(0)
                                        .build()
                        )
                );

        StepVerifier.create(svc.findOrCreateDriver("NewDriver"))
                .expectNextMatches(d -> d.getId() == 1L && d.getName().equals("NewDriver"))
                .verifyComplete();

        verify(driverRepository, times(1)).save(any(Driver.class));
    }

    @Test
    void saveLapTimeByDriverName_saves_with_driver() {
        Driver drv = Driver.builder()
                .id(2L)
                .name("X")
                .team("T")
                .number(5)
                .build();

        when(driverRepository.findByName("X"))
                .thenReturn(Mono.just(drv));

        LapTime lap = LapTime.builder()
                .lapNumber(1)
                .millis(72000L)
                .build();

        LapTime saved = LapTime.builder()
                .id(10L)
                .driverId(2L)
                .lapNumber(1)
                .millis(72000L)
                .build();

        when(lapTimeRepository.save(any(LapTime.class)))
                .thenReturn(Mono.just(saved));

        StepVerifier.create(svc.saveLapTimeByDriverName("X", lap))
                .expectNextMatches(l -> l.getId().equals(10L) && l.getDriverId().equals(2L))
                .verifyComplete();

        verify(lapTimeRepository, times(1)).save(any(LapTime.class));
    }
}
