package com.example.trustmessage.common;

public class CommonHttpResponse {

    private String status;
    private String message;
    private Data data;

    public CommonHttpResponse() {
    }

    public CommonHttpResponse(String status, String message, Data data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private String messageKey;
        private int messageStatus; // commit, prepare, or rollback

        public Data(String messageKey, int messageStatus) {
            this.messageKey = messageKey;
            this.messageStatus = messageStatus;
        }

        public Data() {
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
    }
}
