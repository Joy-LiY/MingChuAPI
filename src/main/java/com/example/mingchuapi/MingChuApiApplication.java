package com.example.mingchuapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 开启定时
 */
@EnableScheduling
@SpringBootApplication
public class MingChuApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MingChuApiApplication.class, args);
    }

}
