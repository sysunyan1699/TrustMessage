package com.example.trustmessage.middleware.service;

import com.example.trustmessage.middleware.model.Message;

import java.time.LocalDateTime;
import java.util.List;

public interface InnerMessageService {

    Message selectByBizIDAndMessageKey(int bizID, String messageKey);

    List<Message> findMessagesForVerify(long minID, int limit);
    List<Message> findMessagesForSend(long minID, int limit);


    boolean handlePrepareMessage(Message m);

    boolean handleCommitMessage(int bizID, String messageKey,int version);

    boolean handleRollbackMessage(int bizID, String messageKey,int version);

    boolean updateMessageStatusByMessageKeyAndBizID(int bizID,
                                                    String messageKey,
                                                    int messageStatus,
                                                    int version);


    boolean updateSendStatusByMessageKeyAndBizID(int bizID,
                                                 String messageKey,
                                                 int sendStatus,
                                                 int version);

    boolean updateVerifyRetryCountAndTime(int bizID,
                                          String messageKey,
                                          int verifyTryCount,
                                          LocalDateTime verifyNextRetryTime,
                                          int version);


    boolean updateSendRetryCountAndTime(int bizID,
                                        String messageKey,
                                        //int originalSendStatus,
                                        int sendTryCount,
                                        LocalDateTime sendNextRetryTime,
                                        int version);


    boolean updateVerifyInfo(int bizID,
                             String messageKey,
                             int messageStatus,
                             int verifyTryCount,
                             LocalDateTime verifyNextRetryTime,
                             int version);

    boolean updateSendInfo(int bizID,
                           String messageKey,
                           //int originalSendStatus,
                           int sendStatus,
                           int sendTryCount,
                           LocalDateTime sendNextRetryTime,
                           int version
    );

}
