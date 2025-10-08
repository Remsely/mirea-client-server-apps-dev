package edu.mirea.remsely.csad.semester7.practice1.task2;

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.*;

public class AsyncSquareCalculator {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter numbers to calculate their squares. Type 'exit' to quit.");

        while (true) {
            String input = scanner.nextLine();

            if ("exit".equalsIgnoreCase(input)) {
                break;
            }

            int number = Integer.parseInt(input);

            Callable<String> task = () -> {
                Thread.sleep(new Random().nextInt(4001) + 1000);
                return String.format("Result for %d: %d", number, (long) number * number);
            };

            Future<String> future = executor.submit(task);

            new Thread(() -> {
                try {
                    String resultMessage = future.get();
                    System.out.println(resultMessage);
                } catch (InterruptedException | ExecutionException e) {
                    System.err.println("A calculation task was interrupted.");
                }
            }).start();
        }

        System.out.println("Shutting down the calculator.");
        executor.shutdown();
        scanner.close();
    }
}
