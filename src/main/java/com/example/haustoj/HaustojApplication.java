package com.example.haustoj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.haustoj.dao")
public class HaustOjApplication {

    public static void main(String[] args) {
        SpringApplication.run(HaustOjApplication.class, args);
    }

}
