package com.example.trustmessage.api;

public enum MessageStateEnum {
    //这个状态将会引起回查,回查后仍然返回这个状态说明消息生产者仍然没有处理完该消息相关的业务逻辑
    PREPARE(1),
    COMMIT_MESSAGE(2),

    ROLLBACK_MESSAGE(3);

    private final int value;
    MessageStateEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static MessageStateEnum valueOf(int value) {
        for (MessageStateEnum m : MessageStateEnum.values()) {
            if (m.getValue() == value) {
                return m;
            }
        }
        throw new IllegalArgumentException("Invalid MessageState value: " + value);
    }

}
