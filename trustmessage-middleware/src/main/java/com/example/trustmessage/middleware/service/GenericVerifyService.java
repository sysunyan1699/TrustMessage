package com.example.trustmessage.middleware.service;


import com.example.trustmessage.middlewareapi.common.MiddlewareMessage;

public interface GenericVerifyService {
    int invoke(int bizID, String messageKey, MiddlewareMessage.VerifyInfo verifyInfo);
}
