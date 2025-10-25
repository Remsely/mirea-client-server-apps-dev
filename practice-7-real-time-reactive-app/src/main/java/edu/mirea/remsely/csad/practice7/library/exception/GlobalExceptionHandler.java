package edu.mirea.remsely.csad.practice7.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookNotFoundException.class)
    public Mono<ResponseEntity<String>> handleBookNotFoundException(BookNotFoundException ex) {
        return Mono.just(
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ex.getMessage())
        );
    }

    @ExceptionHandler(ArithmeticException.class)
    public Mono<ResponseEntity<String>> handleArithmeticException(ArithmeticException ex) {
        return Mono.just(
                ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Arithmetic error: " + ex.getMessage())
        );
    }
}
