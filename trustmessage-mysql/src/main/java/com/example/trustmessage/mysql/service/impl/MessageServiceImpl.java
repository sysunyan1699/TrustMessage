package com.example.trustmessage.mysql.service.impl;

import com.example.trustmessage.mysql.common.MessageStatus;
import com.example.trustmessage.mysql.mapper.MessageMapper;
import com.example.trustmessage.mysql.model.Message;
import com.example.trustmessage.mysql.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageMapper messageMapper;

    @Override
    public boolean prepareMessage(Message message) {
        return messageMapper.insertMessage(message) == 1;
    }

    @Override
    public boolean commitMessage(String messageKey) {
        return updateMessageStatusByMessageKey(
                messageKey,
                MessageStatus.COMMIT.getValue(),
                MessageStatus.PREPARE.getValue());
    }

    @Override
    public boolean rollbackMessage(String messageKey) {
        return updateMessageStatusByMessageKey(
                messageKey,
                MessageStatus.ROLLBACK.getValue(),
                MessageStatus.PREPARE.getValue());
    }

    @Override
    public boolean updateMessageStatusByMessageKey(String messageKey,
                                                   int messageStatus,
                                                   int originalMessageStatus) {
        Map<String, Object> params = new HashMap<>();
        params.put("messageKey", messageKey);
        params.put("messageStatus", messageStatus);
        params.put("originalMessageStatus", originalMessageStatus);
        return doUpdateVerifyInfo(params);
    }


    @Override
    public boolean updateVerifyRetryCountAndTime(String messageKey,
                                                 int originalMessageStatus,
                                                 int verifyTryCount,
                                                 LocalDateTime verifyNextRetryTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("messageKey", messageKey);
        params.put("originalMessageStatus", originalMessageStatus);
        params.put("verifyTryCount", verifyTryCount);
        params.put("verifyNextRetryTime", verifyNextRetryTime);
        return doUpdateVerifyInfo(params);

    }

    @Override
    public boolean updateVerifyInfo(String messageKey,
                                    int messageStatus,
                                    int originalMessageStatus,
                                    int verifyTryCount,
                                    LocalDateTime verifyNextRetryTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("messageKey", messageKey);
        params.put("messageStatus", messageStatus);
        params.put("originalMessageStatus", originalMessageStatus);
        params.put("verifyTryCount", verifyTryCount);
        params.put("verifyNextRetryTime", verifyNextRetryTime);

        return doUpdateVerifyInfo(params);
    }

    private boolean doUpdateVerifyInfo(Map<String, Object> params) {
        return messageMapper.updateVerifyInfo(params) == 1;
    }


    @Override
    public boolean updateSendStatusByMessageKey(String messageKey,
                                                int sendStatus,
                                                int originalSendStatus) {
        Map<String, Object> params = new HashMap<>();
        params.put("messageKey", messageKey);
        params.put("sendStatus", sendStatus);
        params.put("originalSendStatus", originalSendStatus);
        return doUpdateSendInfo(params);
    }

    @Override
    public boolean updateSendRetryCountAndTime(String messageKey,
                                               int originalSendStatus,
                                               int sendTryCount,
                                               LocalDateTime sendNextRetryTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("messageKey", messageKey);
        params.put("originalSendStatus", originalSendStatus);
        params.put("sendTryCount", sendTryCount);
        params.put("sendNextRetryTime", sendNextRetryTime);
        return doUpdateSendInfo(params);
    }

    @Override
    public boolean updateSendInfo(String messageKey,
                                  int sendStatus,
                                  int originalSendStatus,
                                  int sendTryCount,
                                  LocalDateTime sendNextRetryTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("messageKey", messageKey);
        params.put("sendTryCount", sendTryCount);
        params.put("sendNextRetryTime", sendNextRetryTime);
        params.put("sendStatus", sendStatus);
        params.put("originalSendStatus", originalSendStatus);

        return doUpdateSendInfo(params);

    }

    private boolean doUpdateSendInfo(Map<String, Object> params) {
        return messageMapper.updateSendInfo(params) == 1;
    }

}
