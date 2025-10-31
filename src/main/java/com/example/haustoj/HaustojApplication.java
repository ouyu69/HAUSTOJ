package com.example.haustoj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//@EnableSwagger2
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@MapperScan("com.example.haustoj.dao")
public class HaustojApplication {
    /**
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(HaustojApplication.class, args);
    }

}
