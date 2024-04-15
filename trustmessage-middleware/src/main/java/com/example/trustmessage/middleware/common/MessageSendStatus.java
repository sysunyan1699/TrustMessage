package com.example.trustmessage.middleware.common;


// 消息commit, 发送至消息队列的状态
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
