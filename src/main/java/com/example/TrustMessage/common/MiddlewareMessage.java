package com.example.trustmessage.common;

public class MiddlewareMessage {
    private int messageType;
    private String businessTopic;

    private String messageKey;
    private String payload;
    private CallbackInfo callbackInfo;

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getBusinessTopic() {
        return businessTopic;
    }

    public void setBusinessTopic(String businessTopic) {
        this.businessTopic = businessTopic;
    }

    public CallbackInfo getCallbackInfo() {
        return callbackInfo;
    }

    public void setCallbackInfo(CallbackInfo callbackInfo) {
        this.callbackInfo = callbackInfo;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public static class CallbackInfo {
        private int protocolType; //http, thrift,  dubbo
        private String endpoint;

        private String service;
        private String method; // 注意：对于非HTTP协议，这个字段的意义可能不同或不需要

        // 构造函数、Getter和Setter省略

        public int getProtocolType() {
            return protocolType;
        }

        public void setProtocolType(int protocolType) {
            this.protocolType = protocolType;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }
    }
}

