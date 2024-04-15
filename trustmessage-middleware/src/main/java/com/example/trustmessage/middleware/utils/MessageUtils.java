package com.example.trustmessage.middleware.utils;

import com.example.trustmessage.middleware.model.Message;
import com.example.trustmessage.middlewareapi.common.MiddlewareMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

public class MessageUtils {

    public static boolean MiddlewareMessageChecker(MiddlewareMessage m) {

        if (m == null) {
            return false;
        }
        if (m.getForwardTopic() == null || m.getVerifyInfo() == null || m.getMessageKey() == null) {
            return false;
        }

        MiddlewareMessage.VerifyInfo v = m.getVerifyInfo();

        if (v.getRegistryProtocol() == null ||
                v.getUrl() == null || v.getRegistryAddress() == null || v.getVersion() == null) {
            return false;
        }


        return true;
    }

    public static Message MiddlewareMessageConvert2MessageStore(MiddlewareMessage middlewareMessage) {

        Message m = new Message();
        m.setBizID(middlewareMessage.getBizID());
        m.setMessage(middlewareMessage.getMessage());
        m.setMessageKey(middlewareMessage.getMessageKey());
        m.setMessageStatus(middlewareMessage.getMessageStatus());
        m.setForwardTopic(middlewareMessage.getForwardTopic());
        m.setForwardKey(middlewareMessage.getForwardKey());

        // 使用Jackson序列化VerifyInfo
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String verifyInfoJson = objectMapper.writeValueAsString(middlewareMessage.getVerifyInfo());
            m.setVerifyInfo(verifyInfoJson);
        } catch (Exception e) {
            // 处理序列化异常，例如可以设置verifyInfo为null或者抛出一个运行时异常
            m.setVerifyInfo(null);
            // 或抛出自定义异常
            // throw new RuntimeException("Error serializing VerifyInfo", e);
        }

        return m;
    }

    // 可以是其他的重试时间策略
    public static int GetVerifyNextRetryTimeSeconds(int verifyTryCount) {
        return 60 * (verifyTryCount + 1);
    }

    public static int GetSendNextRetryTimeSeconds(int sendTryCount) {
        return 60 * (sendTryCount + 1);
    }


}
