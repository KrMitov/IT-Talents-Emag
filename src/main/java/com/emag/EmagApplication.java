package com.emag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EmagApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmagApplication.class, args);
    }

}
