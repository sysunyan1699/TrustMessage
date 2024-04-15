package com.example.trustmessage.middlewareclient;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDubbo(scanBasePackages = "com.example.trustmessage.middlewareclient.service")
@ComponentScan(basePackages = {"com.example.trustmessage.middlewareclient"})
public class TrustMessageClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(TrustMessageClientApplication.class, args);
    }

}
