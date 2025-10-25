package edu.mirea.remsely.csad.practice7.library.controller;

import edu.mirea.remsely.csad.practice7.library.dto.BookDto;
import edu.mirea.remsely.csad.practice7.library.entity.BookEntity;
import edu.mirea.remsely.csad.practice7.library.mapper.BookMapper;
import edu.mirea.remsely.csad.practice7.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public Flux<BookDto> getAllBooks(@RequestParam(required = false) String author) {
        return bookService.getAllBooks(author)
                .map(entity -> {
                    System.out.println("Fetching book: " + entity.getTitle());
                    return BookMapper.toDto(entity);
                });
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<BookDto>> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id).map(BookMapper::toDto).map(ResponseEntity::ok);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BookDto> createBook(@RequestBody BookDto dto) {
        BookEntity toSave = BookMapper.toEntity(dto);
        return bookService.createBook(toSave).map(BookMapper::toDto);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<BookDto>> updateBook(@PathVariable Long id, @RequestBody BookDto dto) {
        BookEntity toUpdate = BookMapper.toEntity(dto);
        return bookService.updateBook(id, toUpdate)
                .map(BookMapper::toDto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteBook(@PathVariable Long id) {
        return bookService.deleteBook(id);
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<BookDto> streamBooks() {
        Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));
        return bookService.getAllBooks(null)
                .zipWith(interval)
                .map(tuple -> BookMapper.toDto(tuple.getT1()));
    }
}
