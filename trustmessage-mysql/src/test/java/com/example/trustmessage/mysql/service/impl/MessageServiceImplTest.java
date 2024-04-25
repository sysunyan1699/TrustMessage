package com.example.trustmessage.mysql.service.impl;

import com.example.trustmessage.mysql.common.MessageSendStatus;
import com.example.trustmessage.mysql.common.MessageStatus;
import com.example.trustmessage.mysql.model.Message;
import com.example.trustmessage.mysql.utils.MessageUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MessageServiceImplTest {

    @Autowired
    private MessageServiceImpl messageService;

    @Test
    void prepareMessage() {

        Message m = new Message();
        m.setMessage("message4");
        m.setMessageKey("key4");
        m.setMessageStatus(MessageStatus.PREPARE.getValue());
        m.setVerifyNextRetryTime(LocalDateTime.now().plusSeconds(MessageUtils.GetVerifyNextRetryTimeSeconds(1)));
        m.setSendStatus(MessageSendStatus.NOT_SEND.getValue());
        m.setSendNextRetryTime(LocalDateTime.now().plusSeconds(MessageUtils.GetSendNextRetryTimeSeconds(1)));
        assertEquals(true, messageService.prepareMessage(m));
    }

    @Test
    void commitMessage() {
        assertEquals(true, messageService.commitMessage("key4"));
    }

    @Test
    void rollbackMessage() {

        assertEquals(true, messageService.rollbackMessage("key3"));

    }

    @Test
    void updateMessageStatusByMessageKey() {

        //messageService.updateMessageStatusByMessageKey()
    }

    @Test
    void updateVerifyRetryCountAndTime() {

        assertTrue(messageService.updateVerifyRetryCountAndTime("key1",
                2,
                LocalDateTime.now().plusSeconds(600)));
    }

    @Test
    void updateSendStatusByMessageKey() {
        //messageService.updateSendStatusByMessageKey("key1", 2, LocalDateTime.now().plusSeconds(600)))
    }

    @Test
    void updateSendRetryCountAndTime() {
        assertTrue(messageService.updateSendRetryCountAndTime("key1",
                3,
                LocalDateTime.now().plusSeconds(600)));

    }

}