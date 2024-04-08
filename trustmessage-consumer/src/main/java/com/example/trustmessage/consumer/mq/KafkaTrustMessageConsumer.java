package com.example.trustmessage.consumer.mq;

import com.example.trustmessage.common.MessageStatus;
import com.example.trustmessage.common.MiddlewareMessage;
import com.example.trustmessage.consumer.model.Message;
import com.example.trustmessage.consumer.service.MessageService;
import com.example.trustmessage.consumer.utils.MessageUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

public class KafkaTrustMessageConsumer {

    @Autowired
    private MessageService handleMessageService;

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
                MessageUtils.FillRetryInformation(m, 0);
                handleMessageService.handlePrepareMessage(m);
                break;
            case COMMIT:
                System.out.println("Handling commit message: " + record.value());
                handleMessageService.handleCommitMessage(message.getBizID(), message.getMessageKey());
                break;
            case ROLLBACK:
                System.out.println("Handling rollback message: " + record.value());
                handleMessageService.handleRollbackMessage(message.getBizID(), message.getMessageKey());
                break;
            default:
                System.out.println("Unknown message type: " + message.getMessageStatus());
                break;
        }
    }

}

