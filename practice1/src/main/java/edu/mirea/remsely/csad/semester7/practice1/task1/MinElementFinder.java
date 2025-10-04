package edu.mirea.remsely.csad.semester7.practice1.task1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class MinElementFinder {
    public static void main(String[] args) {
        int[] array = new int[10000];
        Random random = new Random();
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(100000);
        }

        measureTime("Sequential", () -> findMinSequentially(array));
        measureTime("Future", () -> findMinWithFuture(array));
        measureTime("Fork/Join Framework", () -> findMinWithForkJoin(array));
    }

    public static int findMinInRange(int[] array, int start, int end) {
        int localMin = Integer.MAX_VALUE;
        for (int i = start; i < end; i++) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            if (array[i] < localMin) {
                localMin = array[i];
            }
        }
        return localMin;
    }

    public static int findMinSequentially(int[] array) {
        return findMinInRange(array, 0, array.length);
    }

    public static int findMinWithFuture(int[] array) {
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        List<Future<Integer>> futures = new ArrayList<>();

        int chunkSize = array.length / numThreads;
        for (int i = 0; i < numThreads; i++) {
            final int start = i * chunkSize;
            final int end = (i == numThreads - 1) ? array.length : (i + 1) * chunkSize;
            futures.add(executor.submit(() -> findMinInRange(array, start, end)));
        }

        int globalMin = futures.stream()
                .mapToInt(f -> {
                    try {
                        return f.get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                })
                .min()
                .orElse(Integer.MAX_VALUE);

        executor.shutdown();
        return globalMin;
    }

    public static int findMinWithForkJoin(int[] array) {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        return pool.invoke(new MinRecursiveTask(array, 0, array.length));
    }

    private static void measureTime(String name, Callable<Integer> task) {
        try {
            long startTime = System.currentTimeMillis();
            int min = task.call();
            long endTime = System.currentTimeMillis();
            System.out.printf("\n--- %s ---\n", name);
            System.out.println("Found min: " + min);
            System.out.println("Execution time: " + (endTime - startTime) + " ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class MinRecursiveTask extends RecursiveTask<Integer> {
        private static final int THRESHOLD = 1000;
        private final int[] array;
        private final int start;
        private final int end;

        public MinRecursiveTask(int[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            if (end - start <= THRESHOLD) {
                return MinElementFinder.findMinInRange(array, start, end);
            } else {
                int mid = start + (end - start) / 2;
                MinRecursiveTask leftTask = new MinRecursiveTask(array, start, mid);
                MinRecursiveTask rightTask = new MinRecursiveTask(array, mid, end);

                leftTask.fork();
                int rightResult = rightTask.compute();
                int leftResult = leftTask.join();

                return Math.min(leftResult, rightResult);
            }
        }
    }
}
