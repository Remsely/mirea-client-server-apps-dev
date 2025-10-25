package edu.mirea.remsely.csad.practice7.library.repository;

import edu.mirea.remsely.csad.practice7.library.entity.BookEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import reactor.test.StepVerifier;

@DataR2dbcTest
class BookRepositoryTest {

    @Autowired
    private BookRepository repository;

    @BeforeEach
    void clean() {
        repository.deleteAll().block();
    }

    @Test
    void save_and_findByAuthor() {
        var b1 = new BookEntity(null, "T1", "A1");
        var b2 = new BookEntity(null, "T2", "A2");
        var b3 = new BookEntity(null, "T3", "A1");

        StepVerifier.create(repository.save(b1))
                .expectNextCount(1)
                .verifyComplete();
        StepVerifier.create(repository.save(b2))
                .expectNextCount(1)
                .verifyComplete();
        StepVerifier.create(repository.save(b3))
                .expectNextCount(1)
                .verifyComplete();

        StepVerifier.create(repository.findByAuthor("A1"))
                .expectNextMatches(b -> b.getAuthor().equals("A1"))
                .expectNextMatches(b -> b.getAuthor().equals("A1"))
                .verifyComplete();
    }
}
