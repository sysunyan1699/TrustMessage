package com.example.trustmessage.consumer.service;

import com.example.trustmessage.common.MessageStatus;
import com.example.trustmessage.consumer.model.Message;

import java.time.LocalDateTime;

public interface MessageService {

    boolean handlePrepareMessage(Message m);

    boolean handleCommitMessage(int bizID, String messageKey);

    boolean handleRollbackMessage(int bizID, String messageKey);

    boolean updateStatusByMessageKeyAndBizID(int bizID, String messageKey, int messageStatus, int originalStatus);


    boolean updateRetryCountAndTime(int bizID, String messageKey, int tryCount,LocalDateTime nextRetryTime);
}
