package com.example.trustmessage.middleware.mq;

import com.example.trustmessage.middlewareapi.common.MessageStatus;
import com.example.trustmessage.middlewareapi.common.MiddlewareMessage;
import com.example.trustmessage.middleware.model.Message;
import com.example.trustmessage.middleware.service.InnerMessageService;
import com.example.trustmessage.middleware.utils.MessageUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import java.time.LocalDateTime;

public class KafkaTrustMessageConsumer {

    @Autowired
    private InnerMessageService innerMessageService;

    @KafkaListener(topics = "yourTopic", groupId = "my-group")
    public void listen(ConsumerRecord<String, String> record) {

        ObjectMapper mapper = new ObjectMapper();
        MiddlewareMessage message = null;
        try {
            message = mapper.readValue(record.value(), MiddlewareMessage.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (MessageUtils.MiddlewareMessageChecker(message)) {
            //打印error 信息， 结束此次处理
            MessageStatus.PREPARE.getValue();

        }
        switch (MessageStatus.valueOf(message.getMessageStatus())) {
            case PREPARE:
                System.out.println("Handling prepare message: " + record.value());
                Message m = MessageUtils.MiddlewareMessageConvert2MessageStore(message);
                m.setVerifyNextRetryTime(LocalDateTime.now().plusSeconds(MessageUtils.GetVerifyNextRetryTimeSeconds(0)));
                innerMessageService.handlePrepareMessage(m);
                break;
            case COMMIT:
                System.out.println("Handling commit message: " + record.value());
                innerMessageService.handleCommitMessage(message.getBizID(), message.getMessageKey());
                break;
            case ROLLBACK:
                System.out.println("Handling rollback message: " + record.value());
                innerMessageService.handleRollbackMessage(message.getBizID(), message.getMessageKey());
                break;
            default:
                System.out.println("Unknown message type: " + message.getMessageStatus());
                break;
        }
    }

}

