package com.example.trustmessage.consumer.mapper;

import com.example.trustmessage.common.MessageStatus;
import com.example.trustmessage.common.MiddlewareMessage;
import com.example.trustmessage.common.VerifyProtocolType;
import com.example.trustmessage.consumer.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.media.jfxmediaimpl.HostUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertFalse;


@SpringBootTest
public class MessageMapperTest {
    @Autowired
    private MessageMapper messageMapper;


    @Test
    public void insertMessageTest() {

        Message m1 = new Message();
        // Populate the message object with test data
        m1.setMessage("Test Message");
        m1.setMessageKey("Key1");
        m1.setBizID(1);
        m1.setMessageStatus(1);
        m1.setForwardTopic("real topic");
        m1.setNextRetryTime(LocalDateTime.now().plusSeconds(60));
        m1.setTryCount(1);

        Message m2 = new Message();
        // Populate the message object with test data
        m2.setMessage("Test Message 2");
        m2.setMessageKey("Key2");
        m2.setBizID(2);
        m2.setMessageStatus(1);
        m2.setForwardTopic("real topic");
        m2.setNextRetryTime(LocalDateTime.now().plusSeconds(60));
        m2.setTryCount(1);

        Message m3 = new Message();
        // Populate the message object with test data
        m3.setMessage("Test Message 3");
        m3.setMessageKey("Key3");
        m3.setBizID(2);
        m3.setMessageStatus(1);
        m3.setForwardTopic("real topic");
        m3.setNextRetryTime(LocalDateTime.now().plusSeconds(60000));
        m3.setTryCount(1);

        MiddlewareMessage.VerifyInfo v1 = new MiddlewareMessage.VerifyInfo(
                VerifyProtocolType.RPC_DUBBO.getValue(),
                "zookeeper",
                "127.0.0.1:2181",
                "dubbo://localhost:12346",
                "1.0.0"
        );

        MiddlewareMessage.VerifyInfo v2 = new MiddlewareMessage.VerifyInfo();
        v2.setProtocolType(VerifyProtocolType.HTTP.getValue());
        v2.setUrl("http://localhost:8082/verifyMessage");

        // 使用Jackson序列化VerifyInfo
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String vJson1 = objectMapper.writeValueAsString(v1);
            String vJson2 = objectMapper.writeValueAsString(v2);

            m1.setVerifyInfo(vJson1);
            m2.setVerifyInfo(vJson2);
            m3.setVerifyInfo(vJson1);
        } catch (Exception e) {
        }


        // Insert the message
        int result = messageMapper.insertMessage(m1);
        assertEquals("insertMessageTest", 1, result);
        int result2 = messageMapper.insertMessage(m2);
        assertEquals("insertMessageTest", 1, result2);

        int result3 = messageMapper.insertMessage(m3);
        assertEquals("insertMessageTest", 1, result3);


    }


    @Test
    public void findByMessageKeyAndBizIDTest() {
        // Message findByMessageKeyAndforwardTopic(Map<String, Object> params);

        Map<String, Object> params = new HashMap<>();
        params.put("bizID", 1);
        params.put("messageKey", "key1");
        Message m = messageMapper.findByMessageKeyAndBizID(params);
        assertEquals("findByMessageKeyAndBizID", 1, m.getId());
    }


    @Test
    public void updateStatusByMessageKeyAndBizIDTest() {
        //    void updateStatusByMessageKeyAndBizID(Map<String, Object> params);

        Map<String, Object> params = new HashMap<>();
        params.put("messageStatus", 3);
        params.put("bizID", 1);
        params.put("messageKey", "key1");
        params.put("originalStatus", 1);

        int result = messageMapper.updateStatusByMessageKeyAndBizID(params);
        assertEquals("updateStatusByMessageKeyAndBizIDTest", 1, result);

        Message m = messageMapper.findByMessageKeyAndBizID(params);
        assertEquals("findByMessageKeyAndBizID", 1, m.getId());
        assertEquals("findByMessageKeyAndBizID", 3, m.getMessageStatus());
    }

    @Test
    public void findMessagesForVerifyTest() {
        //    List<Message> findMessagesForVerify(Map<String, Object> params);
        Map<String, Object> params = new HashMap<>();
        params.put("id", 1);
        params.put("limitCount", 100);

        List<Message> result = messageMapper.findMessagesForVerify(params);

        System.out.println(result);

        assertEquals("findMessagesForVerify", 1, result.size());


    }
}
