package edu.mirea.remsely.csad.semester7.practice2.task2;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class FileGenerator {
    public static void main(String[] args) {
        String fileName = Constants.FILE_NAME;
        long fileSize = 100 * 1024 * 1024;
        byte[] buffer = new byte[8192];
        Random random = new Random();

        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            long bytesWritten = 0;
            while (bytesWritten < fileSize) {
                random.nextBytes(buffer);
                int bytesToWrite = (int) Math.min(buffer.length, fileSize - bytesWritten);
                fos.write(buffer, 0, bytesToWrite);
                bytesWritten += bytesToWrite;
            }
            System.out.println("File " + fileName + " with size 100 Mb created successfully.");
        } catch (IOException e) {
            System.err.printf("Error on file processing: %s %s ", e.getClass().getName(), e.getMessage());
        }
    }
}
