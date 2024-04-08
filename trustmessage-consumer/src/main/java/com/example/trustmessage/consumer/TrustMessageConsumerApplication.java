package com.example.trustmessage.consumer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("com.example.trustmessage.consumer.mapper")
public class TrustMessageConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TrustMessageConsumerApplication.class, args);
    }
}
