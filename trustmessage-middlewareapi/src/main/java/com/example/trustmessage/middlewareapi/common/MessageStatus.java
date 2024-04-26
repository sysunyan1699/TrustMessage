package com.example.trustmessage.middlewareapi.common;

public enum MessageStatus {
    //这个状态将会引起回查,回查后仍然返回这个状态说明消息生产者仍然没有处理完该消息相关的业务逻辑
    PREPARE(1, "prepare"),
    COMMIT(2,"commit"),

    ROLLBACK(3, "rollback"),
    //达到最大消息回查重试次数后消息中间件会设置的状态，业务方不要使用
   VERIFY_FAIL(4, "verify fail");

    private int value;

    private String des;

    MessageStatus(int value, String des) {
        this.value = value;
        this.des = des;
    }

    public int getValue() {
        return this.value;
    }

    public String getDes() {
        return des;
    }

    public static MessageStatus valueOf(int value) {
        for (MessageStatus m : MessageStatus.values()) {
            if (m.getValue() == value) {
                return m;
            }
        }
        throw new IllegalArgumentException("Invalid MessageState value: " + value);
    }

}
