package com.example.trustmessage.producer.service;

import com.example.trustmessage.api.VerifyMessageService;
import com.example.trustmessage.common.MessageStatus;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(interfaceClass = VerifyMessageService.class, version = "1.0.0")
public class VerifyMessageServiceImpl implements VerifyMessageService {
    @Override
    public int verifyMessage(Integer bizID, String messageKey) {
        System.out.println("verifyMessage: " + "bizID=" + bizID + "messageKey=" + messageKey);
        //MessageStatus status = getMessageStatus(messageId);
        return MessageStatus.PREPARE.getValue();
    }
}
