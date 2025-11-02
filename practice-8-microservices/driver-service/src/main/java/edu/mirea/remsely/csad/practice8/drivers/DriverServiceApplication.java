package edu.mirea.remsely.csad.practice8.drivers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//TODO: import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
//TODO: import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
//TODO: @EnableDiscoveryClient
//TODO: @EnableFeignClients
public class DriverServiceApplication {
    static void main(String[] args) {
        SpringApplication.run(DriverServiceApplication.class, args);
    }
}
