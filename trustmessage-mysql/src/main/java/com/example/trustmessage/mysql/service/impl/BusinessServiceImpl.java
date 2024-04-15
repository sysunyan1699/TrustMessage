package com.example.trustmessage.mysql.service.impl;

import com.example.trustmessage.mysql.common.MessageSendStatus;
import com.example.trustmessage.mysql.common.MessageStatus;
import com.example.trustmessage.mysql.model.Message;
import com.example.trustmessage.mysql.service.BusinessService;
import com.example.trustmessage.mysql.service.MessageService;
import com.example.trustmessage.mysql.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BusinessServiceImpl implements BusinessService {

    @Autowired
    private MessageService messageService;

    @Override
    public void business() {

        // 根据业务信息组织消息表需要的信息
        Message m = new Message();
        m.setMessage("message1");
        m.setMessageKey("key1");

        m.setMessageStatus(MessageStatus.PREPARE.getValue());
        m.setVerifyTryCount(1);
        m.setVerifyNextRetryTime(LocalDateTime.now().plusSeconds(MessageUtils.GetVerifyNextRetryTimeSeconds(1)));

        m.setSendStatus(MessageSendStatus.NOT_SEND.getValue());
        m.setSendTryCount(1);
        m.setSendNextRetryTime(LocalDateTime.now().plusSeconds(MessageUtils.GetSendNextRetryTimeSeconds(1)));


        messageService.prepareMessage(m);

        // 业务操作
        boolean businessResult = true;

        // commit 或者rollback 失败可以进行合理重试
        if (businessResult) {
            messageService.commitMessage("key1");
        } else {
            messageService.rollbackMessage("key2");
        }
    }

    @Override
    public MessageStatus verifyMessageStatus(String messageKey) {
        // 查询发现对应的业务逻辑还未执行完成，返回prepare, 消息对消费者依然不可见
        return MessageStatus.PREPARE;
    }
}
