package edu.mirea.remsely.csad.semester7.practice4.f1apiserver.repository;

import edu.mirea.remsely.csad.semester7.practice4.f1apiserver.model.Driver;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface DriverRepository extends ReactiveCrudRepository<Driver, Long> {
    Mono<Driver> findByName(String name);
}
