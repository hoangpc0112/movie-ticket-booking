package com.example.theater;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TheaterApplication {
    public static void main (String[] args) {
        SpringApplication.run(TheaterApplication.class, args);
    }
}
