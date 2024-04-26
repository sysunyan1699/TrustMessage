package com.example.trustmessage.middleware.service.impl;

import com.example.trustmessage.middleware.common.MessageCode;
import com.example.trustmessage.middleware.common.MessageSendStatus;
import com.example.trustmessage.middleware.model.Message;
import com.example.trustmessage.middleware.service.InnerMessageService;
import com.example.trustmessage.middleware.utils.MessageResponseUtil;
import com.example.trustmessage.middleware.utils.MessageUtils;
import com.example.trustmessage.middlewareapi.common.MessageResponse;
import com.example.trustmessage.middlewareapi.common.MiddlewareMessage;
import com.example.trustmessage.middlewareapi.service.MessageService;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@DubboService(interfaceClass = MessageService.class, version = "1.0.0")
public class MessageServiceImpl implements MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Autowired
    private InnerMessageService innerMessageService;

    @Override
    public MessageResponse prepareMessage(MiddlewareMessage mm) {
        if (MessageUtils.MiddlewareMessageChecker(mm)) {
            return MessageResponseUtil.getMessageResponse(MessageCode.ILLEGAL_PARAM, false);
        }

        Message m = MessageUtils.MiddlewareMessageConvert2MessageStore(mm);

        m.setVerifyNextRetryTime(LocalDateTime.now().plusSeconds(MessageUtils.GetVerifyNextRetryTimeSeconds(1)));
        m.setSendStatus(MessageSendStatus.NOT_SEND.getValue());
        m.setSendNextRetryTime(LocalDateTime.now().plusSeconds(MessageUtils.GetSendNextRetryTimeSeconds(1)));
        boolean result = innerMessageService.handlePrepareMessage(m);
        return MessageResponseUtil.getMessageResponse(MessageCode.SUCCESS, result);
    }

    @Override
    public MessageResponse commitMessage(MiddlewareMessage m) {
        boolean result = innerMessageService.handleCommitMessage(m.getBizID(), m.getMessageKey());
        return MessageResponseUtil.getMessageResponse(MessageCode.SUCCESS, result);
    }

    @Override
    public MessageResponse rollBackMessage(MiddlewareMessage m) {
        boolean result = innerMessageService.handleRollbackMessage(m.getBizID(), m.getMessageKey());
        return MessageResponseUtil.getMessageResponse(MessageCode.SUCCESS, result);
    }
}
