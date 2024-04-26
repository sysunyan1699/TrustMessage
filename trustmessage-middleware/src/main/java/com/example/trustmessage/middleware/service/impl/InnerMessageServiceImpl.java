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
        return messageMapper.insertMessage(message) == 1;
    }

    @Override
    public boolean handleCommitMessage(int bizID, String messageKey) {

        return updateMessageStatusByMessageKeyAndBizID(
                bizID,
                messageKey,
                MessageStatus.PREPARE.getValue(),
                MessageStatus.COMMIT.getValue()
        );
    }

    @Override
    public boolean handleRollbackMessage(int bizID, String messageKey) {
        return updateMessageStatusByMessageKeyAndBizID(
                bizID,
                messageKey,
                MessageStatus.PREPARE.getValue(),
                MessageStatus.ROLLBACK.getValue());
    }

    @Override
    public boolean updateMessageStatusByMessageKeyAndBizID(int bizID,
                                                           String messageKey,
                                                           int originalMessageStatus,
                                                           int messageStatus
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("bizID", bizID);
        params.put("messageKey", messageKey);
        params.put("originalMessageStatus", originalMessageStatus);
        params.put("messageStatus", messageStatus);
        return doUpdateVerifyInfo(params);
    }


    @Override
    public boolean updateSendStatusByMessageKeyAndBizID(int bizID,
                                                        String messageKey,
                                                        int originalSendStatus,
                                                        int sendStatus
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("bizID", bizID);
        params.put("messageKey", messageKey);
        params.put("originalSendStatus", originalSendStatus);
        params.put("sendStatus", sendStatus);
        return doUpdateSendInfo(params);
    }

    @Override
    public boolean updateVerifyRetryCountAndTime(int bizID,
                                                 String messageKey,
                                                 int originalMessageStatus,
                                                 int verifyTryCount,
                                                 LocalDateTime verifyNextRetryTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("bizID", bizID);
        params.put("messageKey", messageKey);
        params.put("originalMessageStatus", originalMessageStatus);
        params.put("verifyTryCount", verifyTryCount);
        params.put("verifyNextRetryTime", verifyNextRetryTime);
        return doUpdateVerifyInfo(params);
    }

    @Override
    public boolean updateSendRetryCountAndTime(int bizID,
                                               String messageKey,
                                               int originalSendStatus,
                                               int sendTryCount,
                                               LocalDateTime sendNextRetryTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("bizID", bizID);
        params.put("messageKey", messageKey);
        params.put("originalSendStatus", originalSendStatus);
        params.put("sendTryCount", sendTryCount);
        params.put("sendNextRetryTime", sendNextRetryTime);

        return doUpdateSendInfo(params);
    }


    @Override
    public boolean updateVerifyInfo(int bizID,
                                    String messageKey,
                                    int originalMessageStatus,
                                    int messageStatus,
                                    int verifyTryCount,
                                    LocalDateTime verifyNextRetryTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("bizID", bizID);
        params.put("messageKey", messageKey);
        params.put("originalMessageStatus", originalMessageStatus);
        params.put("verifyTryCount", verifyTryCount);
        params.put("verifyNextRetryTime", verifyNextRetryTime);
        return doUpdateVerifyInfo(params);
    }

    @Override
    public boolean updateSendInfo(int bizID,
                                  String messageKey,
                                  int originalSendStatus,
                                  int sendStatus,
                                  int sendTryCount,
                                  LocalDateTime sendNextRetryTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("bizID", bizID);
        params.put("messageKey", messageKey);
        params.put("originalSendStatus", originalSendStatus);
        params.put("sendStatus", sendStatus);
        params.put("sendTryCount", sendTryCount);
        params.put("sendNextRetryTime", sendNextRetryTime);
        return doUpdateSendInfo(params);
    }

    private boolean doUpdateVerifyInfo(Map<String, Object> params) {
        return messageMapper.updateVerifyInfo(params) == 1;
    }

    private boolean doUpdateSendInfo(Map<String, Object> params) {
        return messageMapper.updateSendInfo(params) == 1;
    }
}
