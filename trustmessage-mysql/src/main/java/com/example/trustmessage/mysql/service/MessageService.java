package com.example.trustmessage.mysql.service;


import com.example.trustmessage.mysql.model.Message;

import java.time.LocalDateTime;

public interface MessageService {

    boolean prepareMessage(Message m);

    boolean commitMessage(String messageKey);

    boolean rollbackMessage(String messageKey);


    boolean updateMessageStatusByMessageKey(String messageKey, int messageStatus, int originalStatus);

    boolean updateSendStatusByMessageKey(String messageKey, int messageStatus, int originalStatus);


    boolean updateVerifyRetryCountAndTime(String messageKey,
                                          int verifyTryCount,
                                          LocalDateTime verifyNextRetryTime);

    boolean updateVerifyInfo(String messageKey,
                             int messageStatus,
                             int originalMessageStatus,
                             int verifyTryCount,
                             LocalDateTime verifyNextRetryTime);

    boolean updateSendRetryCountAndTime(String messageKey,
                                        int sendTryCount,
                                        LocalDateTime sendNextRetryTime);

    boolean updateSendInfo(String messageKey,
                           int sendStatus,
                           int originalSendStatus,
                           int sendTryCount,
                           LocalDateTime sendNextRetryTime
    );


}
