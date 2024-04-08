package com.example.trustmessage.common;

public enum MessageState {
    //这个状态将会引起回查
    PREPARE(1),
    COMMIT_MESSAGE(2),

    ROLLBACK_MESSAGE(3);

    private final int value;
    MessageState(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static MessageState valueOf(int value) {
        for (MessageState m : MessageState.values()) {
            if (m.getValue() == value) {
                return m;
            }
        }
        throw new IllegalArgumentException("Invalid MessageState value: " + value);
    }

}
