package com.example.trustmessage.mysql.common;

public enum MessageSendStatus {

    NOT_SEND(1, "未发送"),
    HAVE_SENDED(2, "已发送");


    private int value;

    private String des;

    MessageSendStatus(int value, String des) {
        this.value = value;
        this.des = des;
    }

    public int getValue() {
        return this.value;
    }

    public String getDes() {
        return des;
    }

    public static MessageSendStatus valueOf(int value) {
        for (MessageSendStatus m : MessageSendStatus.values()) {
            if (m.getValue() == value) {
                return m;
            }
        }
        throw new IllegalArgumentException("Invalid MessageSendStatus value: " + value);
    }
}
