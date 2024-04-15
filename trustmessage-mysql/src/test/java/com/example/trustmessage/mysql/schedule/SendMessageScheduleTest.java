package com.example.trustmessage.mysql.schedule;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SendMessageScheduleTest {


    @Autowired
    private SendMessageSchedule send;

    @Test
    void sendMessageScheduledTask() {

        send.sendMessageScheduledTask();
    }
}