package com.example.trustmessage.middleware.service;

import com.example.trustmessage.middleware.model.Message;

import java.time.LocalDateTime;

public interface InnerMessageService {

    boolean handlePrepareMessage(Message m);

    boolean handleCommitMessage(int bizID, String messageKey);

    boolean handleRollbackMessage(int bizID, String messageKey);

    boolean updateMessageStatusByMessageKeyAndBizID(int bizID,
                                                    String messageKey,
                                                    int originalMessageStatus,
                                                    int messageStatus);


    boolean updateSendStatusByMessageKeyAndBizID(int bizID,
                                                 String messageKey,
                                                 int originalSendStatus,
                                                 int sendStatus);

    boolean updateVerifyRetryCountAndTime(int bizID,
                                          String messageKey,
                                          int originalSendStatus,
                                          int verifyTryCount,
                                          LocalDateTime verifyNextRetryTime);


    boolean updateSendRetryCountAndTime(int bizID,
                                        String messageKey,
                                        int originalSendStatus,
                                        int sendTryCount,
                                        LocalDateTime sendNextRetryTime);


    boolean updateVerifyInfo(int bizID,
                             String messageKey,
                             int originalMessageStatus,
                             int messageStatus,
                             int verifyTryCount,
                             LocalDateTime verifyNextRetryTime);

    boolean updateSendInfo(int bizID,
                           String messageKey,
                           int originalSendStatus,
                           int sendStatus,
                           int sendTryCount,
                           LocalDateTime sendNextRetryTime
    );

}
