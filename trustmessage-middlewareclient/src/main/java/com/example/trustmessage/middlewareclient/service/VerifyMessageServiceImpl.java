package com.example.trustmessage.middlewareclient.service;

import com.example.trustmessage.middlewareapi.common.MessageStatus;
import com.example.trustmessage.middlewareapi.service.VerifyMessageService;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DubboService(interfaceClass = VerifyMessageService.class, version = "1.0.0")
public class VerifyMessageServiceImpl implements VerifyMessageService {

    private static final Logger logger = LoggerFactory.getLogger(VerifyMessageServiceImpl.class);


    @Override
    public int verifyMessage(Integer bizID, String messageKey) {
        // todo 业务逻辑判断该消息的执行状态
        //MessageStatus status = getMessageStatus(messageId);

        int result = MessageStatus.PREPARE.getValue();
        logger.info("verifyMessage, bizID: {}, messageKey: {}, result:{}", bizID, messageKey, result);
        return result;
    }
}
