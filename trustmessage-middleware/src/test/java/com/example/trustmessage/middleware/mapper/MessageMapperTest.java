package com.example.trustmessage.middleware.mapper;

import com.example.trustmessage.middleware.common.MessageSendStatus;
import com.example.trustmessage.middleware.utils.MessageUtils;
import com.example.trustmessage.middlewareapi.common.MiddlewareMessage;
import com.example.trustmessage.middlewareapi.common.VerifyProtocolType;
import com.example.trustmessage.middleware.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.util.AssertionErrors.assertEquals;

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
        m1.setForwardTopic("real topic");
        m1.setMessageStatus(1);
        m1.setSendStatus(MessageSendStatus.NOT_SEND.getValue());

        Message m2 = new Message();
        // Populate the message object with test data
        m2.setMessage("Test Message 2");
        m2.setMessageKey("Key2");
        m2.setBizID(2);
        m2.setMessageStatus(1);
        m2.setForwardTopic("real topic");
        m2.setVerifyNextRetryTime(LocalDateTime.now().plusSeconds(MessageUtils.GetVerifyNextRetryTimeSeconds(1)));
        m2.setSendStatus(MessageSendStatus.NOT_SEND.getValue());
        m2.setSendNextRetryTime(LocalDateTime.now().plusSeconds(MessageUtils.GetSendNextRetryTimeSeconds(1)));

        Message m3 = new Message();
        // Populate the message object with test data
        m3.setMessage("Test Message 3");
        m3.setMessageKey("Key3");
        m3.setBizID(2);
        m3.setMessageStatus(1);
        m3.setForwardTopic("real topic");

        m3.setVerifyTryCount(1);
        m3.setVerifyNextRetryTime(LocalDateTime.now().plusSeconds(MessageUtils.GetVerifyNextRetryTimeSeconds(1)));
        m3.setSendStatus(MessageSendStatus.NOT_SEND.getValue());
        m3.setSendTryCount(1);
        m3.setSendNextRetryTime(LocalDateTime.now().plusSeconds(MessageUtils.GetSendNextRetryTimeSeconds(1)));

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
        Map<String, Object> params = new HashMap<>();
        params.put("bizID", 1);
        params.put("messageKey", "key1");
        Message m = messageMapper.findByMessageKeyAndBizID(params);
        assertEquals("findByMessageKeyAndBizID", 1, m.getId());
    }


    @Test
    public void findMessagesForVerifyTest() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", 0);
        params.put("limitCount", 100);

        List<Message> result = messageMapper.findMessagesForVerify(params);

        System.out.println(result);

        assertEquals("findMessagesForVerify", 2, result.size());

    }
}
