package com.example.trustmessage.middleware.service.impl;

import com.example.trustmessage.middlewareapi.common.MessageStatus;
import com.example.trustmessage.middleware.model.Message;
import com.example.trustmessage.middleware.mapper.MessageMapper;
import com.example.trustmessage.middleware.service.InnerMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class InnerMessageServiceImpl implements InnerMessageService {
    @Autowired
    private MessageMapper messageMapper;

    @Override
    public boolean handlePrepareMessage(Message message) {
        int result = messageMapper.insertMessage(message);
        return result == 1;
    }

    @Override
    public boolean handleCommitMessage(int bizID, String messageKey) {

        return updateMessageStatusByMessageKeyAndBizID(
                bizID,
                messageKey,
                MessageStatus.COMMIT.getValue(),
                MessageStatus.PREPARE.getValue());
    }

    @Override
    public boolean handleRollbackMessage(int bizID, String messageKey) {
        return updateMessageStatusByMessageKeyAndBizID(
                bizID,
                messageKey,
                MessageStatus.ROLLBACK.getValue(),
                MessageStatus.PREPARE.getValue());
    }

    @Override
    public boolean updateMessageStatusByMessageKeyAndBizID(int bizID,
                                                           String messageKey,
                                                           int messageStatus,
                                                           int originalMessageStatus) {
        Map<String, Object> params = new HashMap<>();
        params.put("bizID", bizID);
        params.put("messageKey", messageKey);
        params.put("messageStatus", messageStatus);
        params.put("originalMessageStatus", originalMessageStatus);
        messageMapper.updateMessageStatusByMessageKeyAndBizID(params);
        int result = messageMapper.updateMessageStatusByMessageKeyAndBizID(params);
        return result == 1;
    }


    @Override
    public boolean updateVerifyRetryCountAndTime(int bizID,
                                                 String messageKey,
                                                 int verifyTryCount,
                                                 LocalDateTime verifyNextRetryTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("bizID", bizID);
        params.put("messageKey", messageKey);
        params.put("verifyTryCount", verifyTryCount);
        params.put("verifyNextRetryTime", verifyNextRetryTime);
        int result = messageMapper.updateVerifyRetryCountAndTime(params);
        return result == 1;
    }


    @Override
    public boolean updateSendStatusByMessageKeyAndBizID(int bizID,
                                                        String messageKey,
                                                        int sendStatus,
                                                        int originalSendStatus) {
        Map<String, Object> params = new HashMap<>();
        params.put("bizID", bizID);
        params.put("messageKey", messageKey);
        params.put("sendStatus", sendStatus);
        params.put("originalSendStatus", originalSendStatus);
        int result = messageMapper.updateSendStatusByMessageKeyAndBizID(params);
        return result == 1;
    }

    @Override
    public boolean updateSendRetryCountAndTime(int bizID,
                                               String messageKey,
                                               int sendTryCount,
                                               LocalDateTime sendNextRetryTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("bizID", bizID);
        params.put("messageKey", messageKey);
        params.put("sendTryCount", sendTryCount);
        params.put("sendNextRetryTime", sendNextRetryTime);
        int result = messageMapper.updateVerifyRetryCountAndTime(params);
        return result == 1;
    }


    @Override
    public boolean updateVerifyInfo(int bizID,
                                    String messageKey,
                                    int messageStatus,
                                    int originalMessageStatus,
                                    int verifyTryCount,
                                    LocalDateTime verifyNextRetryTime) {
        return false;
    }

    @Override
    public boolean updateSendInfo(int bizID,
                                  String messageKey,
                                  int sendStatus,
                                  int originalSendStatus,
                                  int sendTryCount,
                                  LocalDateTime sendNextRetryTime) {
        return false;
    }
}
