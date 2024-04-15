package com.example.trustmessage.middleware;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("com.example.trustmessage.middleware.mapper")
public class TrustMessageMiddlewareApplication {
    public static void main(String[] args) {
        SpringApplication.run(TrustMessageMiddlewareApplication.class, args);
    }
}
