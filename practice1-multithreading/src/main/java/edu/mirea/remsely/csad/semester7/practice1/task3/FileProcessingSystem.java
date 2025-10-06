package edu.mirea.remsely.csad.semester7.practice1.task3;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class FileProcessingSystem {
    public static void main(String[] args) {
        BlockingQueue<File> queue = new ArrayBlockingQueue<>(5);

        Thread generatorThread = new Thread(new FileGenerator(queue));
        generatorThread.start();

        Thread xmlHandler = new Thread(new FileHandler(queue, FileType.XML));
        Thread jsonHandler = new Thread(new FileHandler(queue, FileType.JSON));
        Thread xlsHandler = new Thread(new FileHandler(queue, FileType.XLS));

        xmlHandler.start();
        jsonHandler.start();
        xlsHandler.start();

        try {
            TimeUnit.SECONDS.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\n--- SHUTTING DOWN SYSTEM ---");
        generatorThread.interrupt();
        xmlHandler.interrupt();
        jsonHandler.interrupt();
        xlsHandler.interrupt();
    }
}

enum FileType {
    XML, JSON, XLS
}

record File(FileType type, int size) {
}

class FileGenerator implements Runnable {
    private final BlockingQueue<File> queue;
    private final Random random = new Random();

    public FileGenerator(BlockingQueue<File> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                FileType randomType = FileType.values()[random.nextInt(FileType.values().length)];
                int randomSize = random.nextInt(91) + 10;
                File file = new File(randomType, randomSize);

                int delay = random.nextInt(901) + 100;
                Thread.sleep(delay);

                System.out.println("GENERATOR: Produced " + file + ". Queue size: " + (queue.size() + 1));
                queue.put(file);
            }
        } catch (InterruptedException e) {
            System.out.println("Generator was interrupted.");
            Thread.currentThread().interrupt();
        }
    }
}

class FileHandler implements Runnable {
    private final BlockingQueue<File> queue;
    private final FileType handledType;

    public FileHandler(BlockingQueue<File> queue, FileType handledType) {
        this.queue = queue;
        this.handledType = handledType;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                File file = queue.take();

                if (file.type() == this.handledType) {
                    processFile(file);
                } else {
                    System.out.printf("HANDLER-%s: Mismatched file %s. Returning to queue.\n", handledType, file);
                    queue.put(file);
                    Thread.sleep(50);
                }
            }
        } catch (InterruptedException e) {
            System.out.printf("Handler-%s was interrupted.\n", handledType);
            Thread.currentThread().interrupt();
        }
    }

    private void processFile(File file) throws InterruptedException {
        System.out.printf("HANDLER-%s: Starting to process %s.\n", handledType, file);
        long processingTime = (long) file.size() * 7;
        Thread.sleep(processingTime);
        System.out.printf("HANDLER-%s: Finished processing %s in %d ms. Queue size: %d\n", handledType, file, processingTime, queue.size());
    }
}
