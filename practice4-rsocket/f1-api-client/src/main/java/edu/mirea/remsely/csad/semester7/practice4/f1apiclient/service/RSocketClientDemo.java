package edu.mirea.remsely.csad.semester7.practice4.f1apiclient.service;

import edu.mirea.remsely.csad.semester7.practice4.f1apiclient.dto.DriverDto;
import edu.mirea.remsely.csad.semester7.practice4.f1apiclient.dto.LapTimeDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Component
public class RSocketClientDemo {
    private final RSocketRequester requester;

    public RSocketClientDemo(RSocketRequester.Builder builder) {
        this.requester = builder.tcp("localhost", 7000);
    }

    // Request-Response
    public void demoRequestResponse(String driverName) {
        Mono<DriverDto> driverInfoMono = requester
                .route("driver.info")
                .data(driverName)
                .retrieveMono(DriverDto.class);

        driverInfoMono.subscribe(info ->
                log.info("Driver info: {}", info)
        );
    }

    // Request-Stream
    public void demoRequestStream(String driverName) {
        Flux<LapTimeDto> lapFlux = requester
                .route("lap.stream")
                .data(driverName)
                .retrieveFlux(LapTimeDto.class);

        lapFlux.take(5).subscribe(lap ->
                log.info("Lap stream: {}", lap)
        );
    }

    // Channel
    public void demoChannel() {
        Flux<String> driverNames = Flux.just("Lewis Hamilton", "Max Verstappen", "Lando Norris")
                .delayElements(Duration.ofMillis(200));

        Flux<String> updates = requester
                .route("lap.channel")
                .data(driverNames)
                .retrieveFlux(String.class);

        updates.subscribe(update -> log.info("Channel update: {}", update));
    }

    // Fire-and-Forget
    public void demoFireAndForget() {
        LapTimeDto lap = new LapTimeDto(null, 44L, "Lewis Hamilton", 1, 71432L, null);

        requester.route("lap.record")
                .data(lap)
                .send()
                .doOnSuccess(_ -> log.info("Lap recorded for {}", lap.driverName()))
                .subscribe();
    }
}
