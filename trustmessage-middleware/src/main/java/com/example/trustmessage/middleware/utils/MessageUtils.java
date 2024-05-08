package com.example.trustmessage.middleware.utils;

import com.example.trustmessage.middleware.common.MessageSendStatus;
import com.example.trustmessage.middleware.model.Message;
import com.example.trustmessage.middlewareapi.common.MiddlewareMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

public class MessageUtils {

    public static boolean middlewareMessageChecker(MiddlewareMessage m) {

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

    public static Message middlewareMessageConvert2Message(MiddlewareMessage middlewareMessage) throws JsonProcessingException {

        String verifyInfoJson = JsonUtil.toJson(middlewareMessage.getVerifyInfo());

        Message m = new Message();
        m.setBizID(middlewareMessage.getBizID());
        m.setMessage(middlewareMessage.getMessage());
        m.setMessageKey(middlewareMessage.getMessageKey());
        m.setMessageStatus(middlewareMessage.getMessageStatus());
        m.setForwardTopic(middlewareMessage.getForwardTopic());
        m.setForwardKey(middlewareMessage.getForwardKey());

        m.setVerifyInfo(verifyInfoJson);

        m.setVerifyNextRetryTime(LocalDateTime.now().plusSeconds(MessageUtils.getVerifyNextRetryTimeSeconds(0)));
        m.setSendStatus(MessageSendStatus.NOT_SEND.getValue());
        m.setSendNextRetryTime(LocalDateTime.now().plusSeconds(MessageUtils.getSendNextRetryTimeSeconds(0)));

        return m;
    }

    // 可以是其他的重试时间策略
    public static int getVerifyNextRetryTimeSeconds(int verifyTryCount) {
        return 60 * (verifyTryCount + 1);
    }

    public static int getSendNextRetryTimeSeconds(int sendTryCount) {
        return 60 * (sendTryCount + 1);
    }


    public static String getHttpVerifyURL(int bizID, String messageKey, String url) {
        StringBuffer sb = new StringBuffer(url);
        sb.append("?bizID=");
        sb.append(bizID);
        sb.append("&messageKey=");
        sb.append(messageKey);
        return sb.toString();
    }

}
