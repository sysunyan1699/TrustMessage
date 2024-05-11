package com.example.trustmessage.middleware.service.impl;

import com.example.trustmessage.middlewareapi.common.MessageStatus;
import com.example.trustmessage.middleware.model.Message;
import com.example.trustmessage.middleware.mapper.MessageMapper;
import com.example.trustmessage.middleware.service.InnerMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InnerMessageServiceImpl implements InnerMessageService {
    @Autowired
    private MessageMapper messageMapper;

    @Override
    public Message selectByBizIDAndMessageKey(int bizID, String messageKey) {
        Map<String, Object> params = new HashMap<>();
        params.put("bizID", bizID);
        params.put("messageKey", messageKey);
        return messageMapper.findByMessageKeyAndBizID(params);
    }

    @Override
    public List<Message> findMessagesForVerify(long minID, int limit) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", minID);
        params.put("limitCount", limit);
        return messageMapper.findMessagesForVerify(params);
    }

    @Override
    public List<Message> findMessagesForSend(long minID, int limit) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", minID);
        params.put("limitCount", limit);
        return messageMapper.findMessagesForSend(params);
    }


    @Override
    public boolean handlePrepareMessage(Message message) {
        return messageMapper.insertMessage(message) == 1;
    }

    @Override
    public boolean handleCommitMessage(int bizID,
                                       String messageKey,
                                       int version) {

        return updateMessageStatusByMessageKeyAndBizID(
                bizID,
                messageKey,
                MessageStatus.COMMIT.getValue(),
                version
        );
    }

    @Override
    public boolean handleRollbackMessage(int bizID,
                                         String messageKey,
                                         int version) {
        return updateMessageStatusByMessageKeyAndBizID(
                bizID,
                messageKey,
                MessageStatus.ROLLBACK.getValue(),
                version);
    }

    @Override
    public boolean updateMessageStatusByMessageKeyAndBizID(int bizID,
                                                           String messageKey,
                                                           int messageStatus,
                                                           int version
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("bizID", bizID);
        params.put("messageKey", messageKey);
        params.put("messageStatus", messageStatus);
        params.put("version", version);
        return doUpdateVerifyInfo(params);
    }


    @Override
    public boolean updateSendStatusByMessageKeyAndBizID(int bizID,
                                                        String messageKey,
                                                        int sendStatus,
                                                        int version
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("bizID", bizID);
        params.put("messageKey", messageKey);
        params.put("sendStatus", sendStatus);
        params.put("version", version);

        return doUpdateSendInfo(params);
    }

    @Override
    public boolean updateVerifyRetryCountAndTime(int bizID,
                                                 String messageKey,
                                                 int verifyTryCount,
                                                 LocalDateTime verifyNextRetryTime,
                                                 int version) {
        Map<String, Object> params = new HashMap<>();
        params.put("bizID", bizID);
        params.put("messageKey", messageKey);
        params.put("verifyTryCount", verifyTryCount);
        params.put("verifyNextRetryTime", verifyNextRetryTime);
        params.put("version", version);
        return doUpdateVerifyInfo(params);
    }

    @Override
    public boolean updateSendRetryCountAndTime(int bizID,
                                               String messageKey,
                                               int sendTryCount,
                                               LocalDateTime sendNextRetryTime,
                                               int version) {
        Map<String, Object> params = new HashMap<>();
        params.put("bizID", bizID);
        params.put("messageKey", messageKey);
        params.put("sendTryCount", sendTryCount);
        params.put("sendNextRetryTime", sendNextRetryTime);
        params.put("version", version);

        return doUpdateSendInfo(params);
    }


    @Override
    public boolean updateVerifyInfo(int bizID,
                                    String messageKey,
                                    int messageStatus,
                                    int verifyTryCount,
                                    LocalDateTime verifyNextRetryTime,
                                    int version) {
        Map<String, Object> params = new HashMap<>();
        params.put("bizID", bizID);
        params.put("messageKey", messageKey);
        params.put("verifyTryCount", verifyTryCount);
        params.put("verifyNextRetryTime", verifyNextRetryTime);
        params.put("version", version);

        return doUpdateVerifyInfo(params);
    }

    @Override
    public boolean updateSendInfo(int bizID,
                                  String messageKey,
                                  int sendStatus,
                                  int sendTryCount,
                                  LocalDateTime sendNextRetryTime,
                                  int version) {
        Map<String, Object> params = new HashMap<>();
        params.put("bizID", bizID);
        params.put("messageKey", messageKey);
        params.put("sendStatus", sendStatus);
        params.put("sendTryCount", sendTryCount);
        params.put("sendNextRetryTime", sendNextRetryTime);
        params.put("version", version);
        return doUpdateSendInfo(params);
    }

    private boolean doUpdateVerifyInfo(Map<String, Object> params) {
        return messageMapper.updateVerifyInfo(params) == 1;
    }

    private boolean doUpdateSendInfo(Map<String, Object> params) {
        return messageMapper.updateSendInfo(params) == 1;
    }
}
