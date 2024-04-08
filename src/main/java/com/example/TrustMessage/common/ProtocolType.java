package com.example.trustmessage.common;

public enum ProtocolType {
    HTTP(1),
    THRIFT(2),
    DUBBLE(3);

    private final int value;

    ProtocolType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    // 可选：根据整型值查找对应的枚举项
    public static ProtocolType valueOf(int value) {
        for (ProtocolType protocol : ProtocolType.values()) {
            if (protocol.getValue() == value) {
                return protocol;
            }
        }
        throw new IllegalArgumentException("Invalid Protocol value: " + value);
    }
}

