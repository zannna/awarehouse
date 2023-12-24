package com.example.awarehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AwarehouseApplication {

    public static void main(String[] args) {
        SpringApplication.run(AwarehouseApplication.class, args);
    }

}
