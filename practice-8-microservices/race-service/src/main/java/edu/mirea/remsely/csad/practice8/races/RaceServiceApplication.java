package edu.mirea.remsely.csad.practice8.races;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
//@EnableDiscoveryClient
@EnableFeignClients
public class RaceServiceApplication {
    static void main(String[] args) {
        SpringApplication.run(RaceServiceApplication.class, args);
    }
}
