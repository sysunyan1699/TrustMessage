package com.example.trustmessage.middlewareapi.service;

public interface VerifyMessageService {

    // 消息回查接口
    int verifyMessage(Integer bizID,String messageKey);
}
