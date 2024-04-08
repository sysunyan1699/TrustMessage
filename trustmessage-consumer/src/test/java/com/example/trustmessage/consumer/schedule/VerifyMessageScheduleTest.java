package com.example.trustmessage.consumer.schedule;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class VerifyMessageScheduleTest {

    @Autowired
    private VerifyMessageSchedule verifyMessageSchedule;

    @Test
    public void scheduledTaskTest() {
        verifyMessageSchedule.scheduledTask();

    }

}
