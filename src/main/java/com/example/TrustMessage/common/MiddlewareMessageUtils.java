package com.example.trustmessage.common;

import com.example.trustmessage.consumer.model.MessageStore;

public class MiddlewareMessageUtils {


    public static boolean MiddlewareMessageChecker(MiddlewareMessage m) {

        if (m == null) {
            return false;
        }
        if (m.getBusinessTopic() == null || m.getCallbackInfo() == null || m.getMessageKey() == null) {
            return false;
        }

        MiddlewareMessage.CallbackInfo c = m.getCallbackInfo();

        if (c.getEndpoint() == null || (c.getProtocolType() == ProtocolType.HTTP.getValue() && c.getMethod() == null)) {
            return false;
        }

        return true;
    }

    public static MessageStore MiddlewareMessageConvert2MessageStore(MiddlewareMessage m) {

        return new MessageStore();
    }


}
