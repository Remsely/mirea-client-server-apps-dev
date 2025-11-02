package edu.mirea.remsely.csad.practice8.teams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//TODO: import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
//TODO: import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
//TODO: @EnableDiscoveryClient
//TODO: @EnableFeignClients
public class TeamServiceApplication {
    static void main(String[] args) {
        SpringApplication.run(TeamServiceApplication.class, args);
    }
}
