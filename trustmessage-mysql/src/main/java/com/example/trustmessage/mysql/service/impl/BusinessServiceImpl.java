package com.example.trustmessage.mysql.service.impl;

import com.example.trustmessage.mysql.common.MessageSendStatus;
import com.example.trustmessage.mysql.common.MessageStatus;
import com.example.trustmessage.mysql.model.Message;
import com.example.trustmessage.mysql.service.BusinessService;
import com.example.trustmessage.mysql.service.MessageService;
import com.example.trustmessage.mysql.utils.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BusinessServiceImpl implements BusinessService {

    @Autowired
    private MessageService messageService;

    @Override
    public void business() {


        Message m = new Message();
        m.setMessage("message1");
        m.setMessageKey("key1");

        m.setMessageStatus(MessageStatus.PREPARE.getValue());
        m.setVerifyTryCount(1);
        m.setVerifyNextRetryTime(LocalDateTime.now().plusSeconds(MessageUtils.GetVerifyNextRetryTimeSeconds(1)));
        m.setSendStatus(MessageSendStatus.NOT_SEND.getValue());
        m.setSendTryCount(0);
        m.setSendNextRetryTime(LocalDateTime.now().plusSeconds(MessageUtils.GetSendNextRetryTimeSeconds(1)));
        // 1.根据业务信息组织prepare 消息
        messageService.prepareMessage(m);

        // 2.业务操作
        boolean businessResult = dealBusiness();

        // 3. commit 或者rollback 如果状态更新失败等待后续消息回查逻辑更新
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


    public boolean dealBusiness() {
        return true;
    }
}
