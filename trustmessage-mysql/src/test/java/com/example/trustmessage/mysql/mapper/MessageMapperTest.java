package com.example.trustmessage.mysql.mapper;

import com.example.trustmessage.mysql.common.MessageSendStatus;
import com.example.trustmessage.mysql.common.MessageStatus;
import com.example.trustmessage.mysql.model.Message;
import com.example.trustmessage.mysql.utils.MessageUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MessageMapperTest {

    @Autowired
    private MessageMapper messageMapper;

    @Test
    void insertMessage() {

        Message m1 = new Message();
        m1.setMessage("message1");
        m1.setMessageKey("key1");
        m1.setMessageStatus(MessageStatus.PREPARE.getValue());

        m1.setSendStatus(MessageSendStatus.NOT_SEND.getValue());
        m1.setVerifyNextRetryTime(LocalDateTime.now().plusSeconds(MessageUtils.getVerifyNextRetryTimeSeconds(1)));

        m1.setSendNextRetryTime(LocalDateTime.now().plusSeconds(MessageUtils.getSendNextRetryTimeSeconds(1)));

        assertEquals(1, messageMapper.insertMessage(m1));

        Message m2 = new Message();
        m2.setMessage("message2");
        m2.setMessageKey("key2");
        m2.setMessageStatus(MessageStatus.PREPARE.getValue());

        m2.setSendStatus(MessageSendStatus.NOT_SEND.getValue());
        m2.setVerifyNextRetryTime(LocalDateTime.now().plusSeconds(MessageUtils.getVerifyNextRetryTimeSeconds(1)));
        m2.setSendNextRetryTime(LocalDateTime.now().plusSeconds(MessageUtils.getSendNextRetryTimeSeconds(1)));

        assertEquals(1, messageMapper.insertMessage(m2));


        Message m3 = new Message();
        m3.setMessage("message3");
        m3.setMessageKey("key3");
        m3.setMessageStatus(MessageStatus.PREPARE.getValue());

        m3.setSendStatus(MessageSendStatus.NOT_SEND.getValue());
        m3.setVerifyNextRetryTime(LocalDateTime.now().plusSeconds(MessageUtils.getVerifyNextRetryTimeSeconds(1)));
        m3.setSendNextRetryTime(LocalDateTime.now().plusSeconds(MessageUtils.getSendNextRetryTimeSeconds(1)));

        assertEquals(1, messageMapper.insertMessage(m3));
    }

    @Test
    void findByMessageKey() {
        Map<String, Object> params = new HashMap<>();
        params.put("messageKey", "key1");
        Message m = messageMapper.findByMessageKey(params);
        assertEquals(1, m.getId());
    }

    @Test
    void findMessagesForVerify() {
        Map<String, Object> params = new HashMap<>();
        long minId = 0;
        params.put("id", 0);
        params.put("limitCount", 100);
        List<Message> messageList = messageMapper.findMessagesForVerify(params);
        assertEquals(3, messageList.size());
    }

    @Test
    void findMessagesForSend() {
        Map<String, Object> params = new HashMap<>();
        long minId = 1;
        params.put("id", minId);
        params.put("limitCount", 100);
        List<Message> messageList = messageMapper.findMessagesForSend(params);
        assertEquals(1, messageList.size());

    }

    @Test
    void updateSendInfo() {
        Map<String, Object> params = new HashMap<>();
        params.put("messageKey", "key2");
        params.put("sendTryCount", 1);
        params.put("sendNextRetryTime", LocalDateTime.now().plusSeconds(60));
        params.put("originalSendStatus", MessageSendStatus.NOT_SEND.getValue());
        assertEquals(1, messageMapper.updateSendInfo(params));


        params.put("sendTryCount", 2);
        params.put("sendNextRetryTime", LocalDateTime.now().plusSeconds(60));
        params.put("sendStatus", MessageSendStatus.SEND_FAIL.getValue());
        assertEquals(1, messageMapper.updateSendInfo(params));
    }

    @Test
    void updateVerifyInfo() {
        Map<String, Object> params = new HashMap<>();
        params.put("messageKey", "key2");
        params.put("messageStatus", MessageStatus.COMMIT.getValue());
        params.put("originalMessageStatus", MessageStatus.PREPARE.getValue());
        params.put("verifyTryCount", 1);
        //params.put("verifyNextRetryTime", LocalDateTime.now().plusSeconds(60));
        //messageMapper.updateVerifyInfo(params);
        assertEquals(1, messageMapper.updateVerifyInfo(params));
    }
}