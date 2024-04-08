package com.example.trustmessage.producer;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDubbo(scanBasePackages = "com.example.trustmessage.producer.service")
@ComponentScan(basePackages = {"com.example.trustmessage.producer"})


public class TrustMessageProducerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TrustMessageProducerApplication.class, args);
    }

}
