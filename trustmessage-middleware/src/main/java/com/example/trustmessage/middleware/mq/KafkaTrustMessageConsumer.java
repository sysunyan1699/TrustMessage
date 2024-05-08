package com.example.trustmessage.middleware.mq;

import com.example.trustmessage.middlewareapi.common.MessageStatus;
import com.example.trustmessage.middlewareapi.common.MiddlewareMessage;
import com.example.trustmessage.middleware.model.Message;
import com.example.trustmessage.middleware.service.InnerMessageService;
import com.example.trustmessage.middleware.utils.MessageUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import java.time.LocalDateTime;

public class KafkaTrustMessageConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaTrustMessageConsumer.class);

    @Autowired
    private InnerMessageService innerMessageService;

    @KafkaListener(topics = "yourTopic", groupId = "my-group")
    public void listen(ConsumerRecord<String, String> record) {

        ObjectMapper mapper = new ObjectMapper();
        MiddlewareMessage message = null;
        try {
            message = mapper.readValue(record.value(), MiddlewareMessage.class);
        } catch (JsonProcessingException e) {
            logger.error("parse MiddlewareMessage to Message fail,MiddlewareMessage:{},e:{}", record.value(), e);
            return;
        }

        if (!MessageUtils.middlewareMessageChecker(message)) {
            logger.error("MiddlewareMessageChecker fail, message:{}", message);
            //打印error 信息， 结束此次处理
            MessageStatus.PREPARE.getValue();

        }
        switch (MessageStatus.valueOf(message.getMessageStatus())) {
            case PREPARE:
                logger.info("Handling prepare message: {}", record.value());
                Message m;
                try {
                    m = MessageUtils.middlewareMessageConvert2Message(message);
                } catch (JsonProcessingException e) {
                    logger.error("MiddlewareMessageConvert2MessageS to Message fail,MiddlewareMessage:{},e:{}", record.value(), e);
                    return;
                }
                m.setVerifyNextRetryTime(LocalDateTime.now().plusSeconds(MessageUtils.getVerifyNextRetryTimeSeconds(0)));
                innerMessageService.handlePrepareMessage(m);
                break;
            case COMMIT:
                logger.info("Handling commit message: {}", record.value());
                innerMessageService.handleCommitMessage(message.getBizID(), message.getMessageKey());
                break;
            case ROLLBACK:
                logger.info("Handling rollback message: {}", record.value());
                innerMessageService.handleRollbackMessage(message.getBizID(), message.getMessageKey());
                break;
            default:
                logger.error("Unknown message type: {}", record.value());
                break;
        }
    }

}

