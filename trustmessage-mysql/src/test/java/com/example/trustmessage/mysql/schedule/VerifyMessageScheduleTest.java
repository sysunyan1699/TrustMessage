package com.example.trustmessage.mysql.schedule;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class VerifyMessageScheduleTest {
    @Autowired
    VerifyMessageSchedule verify;

    @Test
    void verifyScheduledTask() {
        verify.verifyScheduledTask();
    }
}