package edu.mirea.remsely.csad.practice7.library.repository;

import edu.mirea.remsely.csad.practice7.library.entity.BookEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface BookRepository extends ReactiveCrudRepository<BookEntity, Long> {
    Flux<BookEntity> findByAuthor(String author);
}
