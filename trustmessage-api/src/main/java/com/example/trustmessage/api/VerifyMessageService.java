package com.example.trustmessage.api;

import com.example.trustmessage.common.MessageStatus;

public interface VerifyMessageService {
    int verifyMessage(Integer bizID,String messageKey);
}
