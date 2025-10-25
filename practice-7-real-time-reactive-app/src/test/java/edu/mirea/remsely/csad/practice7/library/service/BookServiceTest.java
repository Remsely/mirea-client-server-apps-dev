package edu.mirea.remsely.csad.practice7.library.service;

import edu.mirea.remsely.csad.practice7.library.entity.BookEntity;
import edu.mirea.remsely.csad.practice7.library.exception.BookNotFoundException;
import edu.mirea.remsely.csad.practice7.library.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class BookServiceTest {

    private final BookRepository repo = Mockito.mock(BookRepository.class);
    private final BookService service = new BookService(repo);

    @Test
    void getAllBooks_filtersByAuthor_whenProvided() {
        Mockito.when(repo.findByAuthor("A1")).thenReturn(Flux.just(new BookEntity(1L, "T1", "A1")));

        StepVerifier.create(service.getAllBooks("A1"))
                .expectNextMatches(b -> b.getId() == 1L && b.getAuthor().equals("A1"))
                .verifyComplete();
    }

    @Test
    void getAllBooks_returnsAll_whenAuthorNull() {
        Mockito.when(repo.findAll()).thenReturn(Flux.just(
                new BookEntity(1L, "T1", "A1"),
                new BookEntity(2L, "T2", "A2")
        ));

        StepVerifier.create(service.getAllBooks(null))
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void getBookById_returnsEntity() {
        Mockito.when(repo.findById(1L)).thenReturn(Mono.just(new BookEntity(1L, "T1", "A1")));

        StepVerifier.create(service.getBookById(1L))
                .expectNextMatches(b -> b.getId() == 1L)
                .verifyComplete();
    }

    @Test
    void getBookById_errors_whenMissing() {
        Mockito.when(repo.findById(99L)).thenReturn(Mono.empty());

        StepVerifier.create(service.getBookById(99L))
                .expectError(BookNotFoundException.class)
                .verify();
    }

    @Test
    void createBook_saves() {
        var entity = new BookEntity(null, "T1", "A1");
        Mockito.when(repo.save(entity)).thenReturn(Mono.just(new BookEntity(10L, "T1", "A1")));

        StepVerifier.create(service.createBook(entity))
                .expectNextMatches(b -> b.getId() == 10L)
                .verifyComplete();
    }

    @Test
    void updateBook_updatesFields_whenExists() {
        Mockito.when(repo.findById(1L)).thenReturn(Mono.just(new BookEntity(1L, "T1", "A1")));
        Mockito.when(repo.save(Mockito.any())).then(inv -> Mono.just((BookEntity) inv.getArgument(0)));

        StepVerifier.create(service.updateBook(1L, new BookEntity(null, "TN", "AN")))
                .expectNextMatches(b -> b.getId() == 1L && b.getTitle().equals("TN") && b.getAuthor().equals("AN"))
                .verifyComplete();
    }

    @Test
    void updateBook_errors_whenMissing() {
        Mockito.when(repo.findById(42L)).thenReturn(Mono.empty());

        StepVerifier.create(service.updateBook(42L, new BookEntity(null, "T", "A")))
                .expectError(BookNotFoundException.class)
                .verify();
    }

    @Test
    void deleteBook_delegates() {
        Mockito.when(repo.deleteById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(service.deleteBook(1L))
                .verifyComplete();
    }
}

