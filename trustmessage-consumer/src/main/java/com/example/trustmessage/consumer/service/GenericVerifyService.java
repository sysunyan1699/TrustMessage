package com.example.trustmessage.consumer.service;

import com.example.trustmessage.common.MessageStatus;
import com.example.trustmessage.common.MiddlewareMessage;

public interface GenericVerifyService {
    int invoke(int bizID, String messageKey, MiddlewareMessage.VerifyInfo verifyInfo);
}
