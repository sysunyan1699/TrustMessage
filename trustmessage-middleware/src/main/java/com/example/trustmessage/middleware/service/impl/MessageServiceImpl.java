package com.example.trustmessage.middleware.service.impl;

import com.example.trustmessage.middleware.common.MessageCode;
import com.example.trustmessage.middleware.model.Message;
import com.example.trustmessage.middleware.service.InnerMessageService;
import com.example.trustmessage.middleware.utils.MessageResponseUtil;
import com.example.trustmessage.middleware.utils.MessageUtils;
import com.example.trustmessage.middlewareapi.common.MessageResponse;
import com.example.trustmessage.middlewareapi.common.MessageStatus;
import com.example.trustmessage.middlewareapi.common.MiddlewareMessage;
import com.example.trustmessage.middlewareapi.service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService(interfaceClass = MessageService.class, version = "1.0.0")
public class MessageServiceImpl implements MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Autowired
    private InnerMessageService innerMessageService;

    @Override
    public MessageResponse prepareMessage(MiddlewareMessage mm) {
        if (MessageUtils.middlewareMessageChecker(mm)) {
            return MessageResponseUtil.getMessageResponse(MessageCode.ILLEGAL_PARAM, false);
        }

        Message m;
        try {
            m = MessageUtils.middlewareMessageConvert2Message(mm);
        } catch (JsonProcessingException e) {
            logger.error("prepareMessage MiddlewareMessageConvert2Message MiddlewareMessage:{},error:{}", mm, e);
            return MessageResponseUtil.getMessageResponse(MessageCode.ILLEGAL_PARAM, false);
        }
        boolean result = innerMessageService.handlePrepareMessage(m);
        return MessageResponseUtil.getMessageResponse(MessageCode.SUCCESS, result);
    }

    @Override
    public MessageResponse commitMessage(MiddlewareMessage m) {
        //todo 优化措施
        Message message = innerMessageService.selectByBizIDAndMessageKey(m.getBizID(), m.getMessageKey());
        if (message == null) {
            MessageResponseUtil.getMessageResponse(MessageCode.MESSAGE_NOT_EXIST, false);
        }
        if (message.getMessageStatus() == MessageStatus.COMMIT.getValue()) {
            return MessageResponseUtil.getMessageResponse(MessageCode.SUCCESS, true);
        }

        if (message.getMessageStatus() == MessageStatus.ROLLBACK.getValue()) {
            return MessageResponseUtil.getMessageResponse(MessageCode.MESSAGE_ROLLBACKED, false);
        }
        boolean result = innerMessageService.handleCommitMessage(m.getBizID(),
                m.getMessageKey(),
                message.getVersion());
        return MessageResponseUtil.getMessageResponse(MessageCode.SUCCESS, result);
    }

    @Override
    public MessageResponse rollBackMessage(MiddlewareMessage m) {
        //todo 优化措施
        Message message = innerMessageService.selectByBizIDAndMessageKey(m.getBizID(), m.getMessageKey());
        if (message == null) {
            MessageResponseUtil.getMessageResponse(MessageCode.MESSAGE_NOT_EXIST, false);
        }
        if (message.getMessageStatus() == MessageStatus.ROLLBACK.getValue()) {
            return MessageResponseUtil.getMessageResponse(MessageCode.SUCCESS, true);
        }

        if (message.getMessageStatus() == MessageStatus.COMMIT.getValue()) {
            return MessageResponseUtil.getMessageResponse(MessageCode.MESSAGE_COMMITED, false);
        }
        boolean result = innerMessageService.handleRollbackMessage(m.getBizID(),
                m.getMessageKey(),
                message.getVersion());
        return MessageResponseUtil.getMessageResponse(MessageCode.SUCCESS, result);
    }
}
