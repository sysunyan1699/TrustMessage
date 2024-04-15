package com.example.trustmessage.middlewareapi.common;

public class MiddlewareMessage {

    // 要给到业务方的真正消息
    private String message;

    // 用于消息回查的业务唯一标识
    private String messageKey;

    private int bizID;
    private int messageStatus;

    private String forwardTopic;

    // 向业务方转发时需要指定的key，没有则说明按照kafka 默认分区策略进行分区
    private String forwardKey;

    private VerifyInfo verifyInfo;

    public MiddlewareMessage() {
    }

    public MiddlewareMessage(String message, String messageKey, int messageStatus, String forwardTopic, String forwardKey, VerifyInfo verifyInfo) {
        this.message = message;
        this.messageKey = messageKey;
        this.messageStatus = messageStatus;
        this.forwardTopic = forwardTopic;
        this.forwardKey = forwardKey;
        this.verifyInfo = verifyInfo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public int getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(int messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getForwardTopic() {
        return forwardTopic;
    }

    public void setForwardTopic(String forwardTopic) {
        this.forwardTopic = forwardTopic;
    }

    public String getForwardKey() {
        return forwardKey;
    }

    public void setForwardKey(String forwardKey) {
        this.forwardKey = forwardKey;
    }

    public VerifyInfo getVerifyInfo() {
        return verifyInfo;
    }

    public void setVerifyInfo(VerifyInfo verifyInfo) {
        this.verifyInfo = verifyInfo;
    }

    public int getBizID() {
        return bizID;
    }

    public void setBizID(int bizID) {
        this.bizID = bizID;
    }

    public static class VerifyInfo {
        private int  protocolType; // 1-http, 2-rpc-dubbo
        private String registryProtocol;
        private String registryAddress;
        private String url;
        private String version;


        public int getProtocolType() {
            return protocolType;
        }

        public void setProtocolType(int protocolType) {
            this.protocolType = protocolType;
        }

        public String getRegistryProtocol() {
            return registryProtocol;
        }

        public void setRegistryProtocol(String registryProtocol) {
            this.registryProtocol = registryProtocol;
        }

        public String getRegistryAddress() {
            return registryAddress;
        }

        public void setRegistryAddress(String registryAddress) {
            this.registryAddress = registryAddress;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public VerifyInfo() {
        }

        public VerifyInfo(int protocolType, String registryProtocol, String registryAddress, String url, String version) {
            this.protocolType = protocolType;
            this.registryProtocol = registryProtocol;
            this.registryAddress = registryAddress;
            this.url = url;
            this.version = version;
        }
    }
}

