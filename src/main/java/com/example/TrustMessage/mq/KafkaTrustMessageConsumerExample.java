package com.example.trustmessage.consumer.mq;

import com.example.trustmessage.common.MessageState;
import com.example.trustmessage.common.MiddlewareMessage;
import com.example.trustmessage.common.MiddlewareMessageUtils;
import com.example.trustmessage.common.ProtocolType;
import com.example.trustmessage.consumer.model.MessageStore;
import com.example.trustmessage.consumer.service.TrustMessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Message;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import java.nio.charset.StandardCharsets;

public class KafkaTrustMessageConsumerExample {


    @Autowired
    private TrustMessageService trustMessageService;

    //在 Kafka 中，每条消息都是一个 ConsumerRecord 类型，
    // 包含了消息的 key、value（消息体）、partition、offset 等信息。
    // 如果你想在 @KafkaListener 注解的方法中获取除了消息体以外的其他信息（如 key），
    // 你可以通过更改方法的参数来接收整个 ConsumerRecord 对象，而不是仅接收消息体。
    @KafkaListener(topics = "yourTopic", groupId = "my-group")
    public void listen(ConsumerRecord<String, String> record) {

        Header messageTypeHeader = record.headers().lastHeader("MessageType");
        String businessTopic = new String(record.headers().lastHeader("BusinessTopic").value(), StandardCharsets.UTF_8);

        if (messageTypeHeader != null) {

            ObjectMapper mapper = new ObjectMapper();
            MiddlewareMessage message = null;
            try {
                message = mapper.readValue(record.value(), MiddlewareMessage.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            if (MiddlewareMessageUtils.MiddlewareMessageChecker(message)) {
                //打印error 信息， 结束此次处理
                MessageState.PREPARE.getValue();

            }
            switch (MessageState.valueOf(message.getMessageType())) {
                case PREPARE:
                    System.out.println("Handling prepare message: " + record.value());

                    MessageStore ms = MiddlewareMessageUtils.MiddlewareMessageConvert2MessageStore(message);
                    trustMessageService.handlePrepareMessage(ms);
                    break;
                case COMMIT_MESSAGE:
                    System.out.println("Handling commit message: " + record.value());
                    trustMessageService.handleCommitMessage(message.getMessageKey());
                    break;
                case ROLLBACK_MESSAGE:
                    System.out.println("Handling rollback message: " + record.value());
                    trustMessageService.handleRollbackMessage(message.getMessageKey());
                    break;
                default:
                    System.out.println("Unknown message type: " + message.getMessageType());
                    break;
            }
        } else {
            System.out.println("Message type header missing");
        }
        // 处理消息...
    }

}

