package edu.mirea.remsely.csad.semester7.practice4.f1apiclient;

import edu.mirea.remsely.csad.semester7.practice4.f1apiclient.service.RSocketClientDemo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class F1ApiClientApplication implements CommandLineRunner {
    private final RSocketClientDemo clientDemo;

    static void main(String[] args) {
        SpringApplication.run(F1ApiClientApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        clientDemo.demoRequestResponse("Lewis Hamilton");
        Thread.sleep(1000);

        clientDemo.demoRequestStream("Max Verstappen");
        Thread.sleep(3000);

        clientDemo.demoChannel();
        Thread.sleep(3000);

        clientDemo.demoFireAndForget();
        Thread.sleep(500);
    }
}
