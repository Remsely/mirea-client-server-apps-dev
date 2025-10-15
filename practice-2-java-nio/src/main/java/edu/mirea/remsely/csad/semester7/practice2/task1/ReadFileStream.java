package edu.mirea.remsely.csad.semester7.practice2.task1;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class ReadFileStream {
    public static void main(String[] args) {
        String fileName = "practice-2-java-nio/src/main/java/edu/mirea/remsely/csad/semester7/practice2/task1/task1_file.txt";
        Path path = Path.of(fileName);

        try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8)) {
            lines.forEach(System.out::println);
        } catch (IOException e) {
            System.err.printf("Error on file processing: %s %s ", e.getClass().getName(), e.getMessage());
        }
    }
}
