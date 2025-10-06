package edu.mirea.remsely.csad.semester7.practice4.f1apiserver.repository;

import edu.mirea.remsely.csad.semester7.practice4.f1apiserver.model.LapTime;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface LapTimeRepository extends ReactiveCrudRepository<LapTime, Long> {
    Flux<LapTime> findAllByDriverIdOrderByLapNumberAsc(Long driverId);
}
