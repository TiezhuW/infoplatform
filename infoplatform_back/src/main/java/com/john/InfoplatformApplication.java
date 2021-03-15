package com.john;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.john.repository")
public class InfoplatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(InfoplatformApplication.class, args);
    }

}
