package edu.mirea.remsely.csad.practice7.library.controller;

import edu.mirea.remsely.csad.practice7.library.dto.BookDto;
import edu.mirea.remsely.csad.practice7.library.entity.BookEntity;
import edu.mirea.remsely.csad.practice7.library.service.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;

@WebFluxTest(controllers = BookController.class)
@Import(BookControllerTest.TestConfig.class)
class BookControllerTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        BookService bookService() {
            return Mockito.mock(BookService.class);
        }
    }

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private BookService bookService;

    @Test
    void getAllBooks_shouldReturnDtos() {
        var e1 = new BookEntity(1L, "T1", "A1");
        var e2 = new BookEntity(2L, "T2", "A2");

        Mockito.when(bookService.getAllBooks(isNull())).thenReturn(Flux.just(e1, e2));

        webTestClient.get()
                .uri("/books")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBodyList(BookDto.class)
                .hasSize(2)
                .contains(new BookDto(1L, "T1", "A1"), new BookDto(2L, "T2", "A2"));
    }

    @Test
    void getBookById_shouldReturnDto() {
        var e1 = new BookEntity(1L, "T1", "A1");
        Mockito.when(bookService.getBookById(1L)).thenReturn(Mono.just(e1));

        webTestClient.get()
                .uri("/books/{id}", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class)
                .isEqualTo(new BookDto(1L, "T1", "A1"));
    }

    @Test
    void createBook_shouldReturnCreatedDto() {
        var toCreate = new BookDto(null, "T1", "A1");
        var saved = new BookEntity(10L, "T1", "A1");
        Mockito.when(bookService.createBook(any())).thenReturn(Mono.just(saved));

        webTestClient.post()
                .uri("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(toCreate)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(BookDto.class)
                .isEqualTo(new BookDto(10L, "T1", "A1"));
    }

    @Test
    void updateBook_shouldReturnUpdatedDto() {
        var updated = new BookEntity(1L, "TN", "AN");
        Mockito.when(bookService.updateBook(eq(1L), any())).thenReturn(Mono.just(updated));

        webTestClient.put()
                .uri("/books/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new BookDto(null, "TN", "AN"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class)
                .isEqualTo(new BookDto(1L, "TN", "AN"));
    }

    @Test
    void deleteBook_shouldReturnNoContent() {
        Mockito.when(bookService.deleteBook(1L)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/books/{id}", 1L)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void streamBooks_shouldEmitDtos() {
        var e1 = new BookEntity(1L, "T1", "A1");
        var e2 = new BookEntity(2L, "T2", "A2");
        Mockito.when(bookService.getAllBooks(isNull())).thenReturn(Flux.just(e1, e2));

        var result = webTestClient.get()
                .uri("/books/stream")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .returnResult(BookDto.class);

        List<BookDto> firstTwo = result.getResponseBody()
                .take(2)
                .timeout(Duration.ofSeconds(3))
                .collectList()
                .block();

        assert firstTwo != null;
        org.assertj.core.api.Assertions.assertThat(firstTwo)
                .containsExactly(new BookDto(1L, "T1", "A1"), new BookDto(2L, "T2", "A2"));
    }
}
