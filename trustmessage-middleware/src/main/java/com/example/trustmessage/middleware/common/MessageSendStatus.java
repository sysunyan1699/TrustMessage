package com.example.trustmessage.middleware.common;


// 消息commit, 发送至消息队列的状态
public enum MessageSendStatus {

    NOT_SEND(0, "未发送"),
    SEND_SUCCESS(1, "发送成功"),

    //发送重试达最大次数， 需要人工介入处理
    SEND_FAIL(2, "发送失败");

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
