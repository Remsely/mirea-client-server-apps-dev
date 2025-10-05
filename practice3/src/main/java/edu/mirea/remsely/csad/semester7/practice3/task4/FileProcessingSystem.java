package edu.mirea.remsely.csad.semester7.practice3.task4;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class FileProcessingSystem {
    public static void main(String[] args) {
        BlockingQueue<File> queue = new ArrayBlockingQueue<>(5);

        startProducer(queue, 30);

        Flowable<File> source = Flowable.<File>create(emitter -> {
                    try {
                        while (!emitter.isCancelled()) {
                            File f = queue.take();
                            if (f.type == FileType.POISON) {
                                emitter.onComplete();
                                break;
                            } else {
                                System.out.printf("[Source] take(): %s (queueSize=%d)%n", f, queue.size());
                                emitter.onNext(f);
                            }
                        }
                    } catch (InterruptedException e) {
                        emitter.onError(e);
                        Thread.currentThread().interrupt();
                    }
                }, BackpressureStrategy.BUFFER)
                .publish()
                .refCount();

        subscribeHandler(source, FileType.XML, "XML-Handler");
        subscribeHandler(source, FileType.JSON, "JSON-Handler");
        subscribeHandler(source, FileType.XLS, "XLS-Handler");

        System.out.println("File processing system started... waiting until all files processed.");

        source.ignoreElements().blockingAwait();

        System.out.println("File processing system stopped.");
    }

    private static void startProducer(BlockingQueue<File> queue, int totalFiles) {
        Thread producer = new Thread(() -> {
            Random rnd = new Random();
            FileType[] types = new FileType[]{FileType.XML, FileType.JSON, FileType.XLS};
            try {
                for (int i = 0; i < totalFiles; i++) {
                    FileType type = types[rnd.nextInt(types.length)];
                    int size = rnd.nextInt(91) + 10;
                    File f = new File(type, size);
                    queue.put(f);
                    System.out.printf("[Producer] put(): %s (queueSize=%d)%n", f, queue.size());
                    long sleep = 100 + rnd.nextInt(901);
                    Thread.sleep(sleep);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("[Producer] interrupted");
            } finally {
                try {
                    queue.put(new File(FileType.POISON, 0));
                    System.out.println("[Producer] sent POISON and finished.");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "ProducerThread");
        producer.setDaemon(true);
        producer.start();
    }

    private static void subscribeHandler(Flowable<File> source, FileType handledType, String handlerName) {
        source.doOnNext(f -> {
                    if (f.type != handledType) {
                        System.out.printf("[%s] skipped %s%n", handlerName, f);
                    }
                })
                .filter(f -> f.type == handledType)
                .observeOn(Schedulers.io())
                .subscribe(
                        f -> processFile(f, handlerName),
                        err -> System.err.println("[" + handlerName + "] Error: " + err),
                        () -> System.out.println("[" + handlerName + "] Completed")
                );
    }

    private static void processFile(File file, String handlerName) throws InterruptedException {
        System.out.printf("[%s] -> Started processing %s%n", handlerName, file);
        TimeUnit.MILLISECONDS.sleep(file.size * 7L);
        System.out.printf("[%s] <- Finished processing %s%n", handlerName, file);
    }

    private record File(FileType type, int size) {
    }

    private enum FileType {
        XML, JSON, XLS, POISON
    }
}
