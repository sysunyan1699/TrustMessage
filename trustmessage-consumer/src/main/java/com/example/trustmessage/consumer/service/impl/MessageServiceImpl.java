package com.example.trustmessage.consumer.service.impl;

import com.example.trustmessage.common.MessageStatus;
import com.example.trustmessage.consumer.model.Message;
import com.example.trustmessage.consumer.mapper.MessageMapper;
import com.example.trustmessage.consumer.service.MessageService;
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
    public boolean handlePrepareMessage(Message message) {
        int result = messageMapper.insertMessage(message);
        return result == 1;
    }

    @Override
    public boolean handleCommitMessage(int bizID, String messageKey) {

        return updateStatusByMessageKeyAndBizID(
                bizID,
                messageKey,
                MessageStatus.COMMIT.getValue(),
                MessageStatus.PREPARE.getValue());
    }

    @Override
    public boolean handleRollbackMessage(int bizID, String messageKey) {
        return updateStatusByMessageKeyAndBizID(
                bizID,
                messageKey,
                MessageStatus.ROLLBACK.getValue(),
                MessageStatus.PREPARE.getValue());
    }

    @Override
    public boolean updateStatusByMessageKeyAndBizID(int bizID, String messageKey, int messageStatus, int originalStatus) {
        Map<String, Object> params = new HashMap<>();
        params.put("messageStatus", messageStatus);
        params.put("bizID", bizID);
        params.put("messageKey", messageKey);
        params.put("originalStatus", originalStatus);
        messageMapper.updateStatusByMessageKeyAndBizID(params);
        int result = messageMapper.updateStatusByMessageKeyAndBizID(params);
        return result == 1;
    }


    @Override
    public boolean updateRetryCountAndTime(int bizID, String messageKey, int tryCount, LocalDateTime nextRetryTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("bizID", bizID);
        params.put("messageKey", messageKey);
        params.put("tryCount", tryCount);
        params.put("nextRetryTime", nextRetryTime);
        int result = messageMapper.updateRetryCountAndTime(params);
        return result == 1;
    }
}
