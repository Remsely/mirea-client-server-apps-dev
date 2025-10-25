package edu.mirea.remsely.csad.practice7.library.service;

import edu.mirea.remsely.csad.practice7.library.entity.BookEntity;
import edu.mirea.remsely.csad.practice7.library.exception.BookNotFoundException;
import edu.mirea.remsely.csad.practice7.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public Flux<BookEntity> getAllBooks(String author) {
        return (author != null) ? bookRepository.findByAuthor(author) : bookRepository.findAll();
    }

    public Mono<BookEntity> getBookById(Long id) {
        return bookRepository.findById(id)
                .switchIfEmpty(Mono.error(new BookNotFoundException(id)));
    }

    public Mono<BookEntity> createBook(BookEntity book) {
        return bookRepository.save(book);
    }

    public Mono<BookEntity> updateBook(Long id, BookEntity updatedBook) {
        return bookRepository.findById(id)
                .flatMap(existingBook -> {
                    existingBook.setTitle(updatedBook.getTitle());
                    existingBook.setAuthor(updatedBook.getAuthor());
                    return bookRepository.save(existingBook);
                })
                .switchIfEmpty(Mono.error(new BookNotFoundException(id)));
    }

    public Mono<Void> deleteBook(Long id) {
        return bookRepository.deleteById(id);
    }
}
