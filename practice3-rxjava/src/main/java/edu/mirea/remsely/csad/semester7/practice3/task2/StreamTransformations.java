package edu.mirea.remsely.csad.semester7.practice3.task2;

import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StreamTransformations {
    public static void main(String[] args) {
        System.out.println("=== 2.1.1 - 1000 squared numbers ===");
        var sourceIntStream = randomIntStream(0, 1000, 1000);
        System.out.println("Source stream: " + sourceIntStream.take(10).toList().blockingGet());
        var result1 = squaredStream(sourceIntStream);
        System.out.println("Result: " + result1.take(10).toList().blockingGet());
        System.out.println();

        System.out.println("=== 2.2.1 - 2 zipped streams (letters and numbers 2) ===");
        var sourceLettersStream = randomLettersStream(0, 26, 1000);
        System.out.println("Source letters stream: " + sourceLettersStream.take(10).toList().blockingGet());
        System.out.println("Source numbers stream: " + sourceIntStream.take(10).toList().blockingGet());
        var result2 = zippedLettersAndDigitsStream(sourceLettersStream, sourceIntStream);
        System.out.println("Result: " + result2.take(10).toList().blockingGet());
        System.out.println();

        System.out.println("=== 2.3.1 - stream with skipped 3 elements ===");
        var source10NumbersStream = randomIntStream(0, 1000, 10);
        System.out.println("Source stream: " + source10NumbersStream.toList().blockingGet());
        var result3 = skippedStream(source10NumbersStream);
        System.out.println("Result: " + result3.toList().blockingGet());
    }

    private static Observable<Long> squaredStream(Observable<Integer> sourceStream) {
        return sourceStream.map(n -> (long) n * n);
    }

    private static Observable<String> zippedLettersAndDigitsStream(
            Observable<String> stringSourceStream,
            Observable<Integer> intSourceStream
    ) {
        return Observable.zip(stringSourceStream, intSourceStream, (letter, digit) -> letter + digit);
    }

    private static Observable<Integer> skippedStream(Observable<Integer> sourceStream) {
        return sourceStream.skip(3);
    }

    private static Observable<Integer> randomIntStream(int from, int to, int count) {
        var list = IntStream.range(0, count)
                .map(i -> ThreadLocalRandom.current().nextInt(from, to))
                .boxed()
                .collect(Collectors.toList());
        return Observable.fromIterable(list);
    }

    private static Observable<String> randomLettersStream(int from, int to, int count) {
        var list = IntStream.range(0, count)
                .map(i -> ThreadLocalRandom.current().nextInt(from, to))
                .mapToObj(n -> String.valueOf((char) ('A' + n)))
                .collect(Collectors.toList());
        return Observable.fromIterable(list);
    }
}
