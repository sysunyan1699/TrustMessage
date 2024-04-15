package com.example.trustmessage.middlewareapi.common;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum VerifyProtocolType {
    HTTP(1),
    RPC_DUBBO(2);

    private int value;

    VerifyProtocolType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    @JsonCreator
    public static VerifyProtocolType valueOf(int value) {
        for (VerifyProtocolType v : VerifyProtocolType.values()) {
            if (v.getValue() == value) {
                return v;
            }
        }
        throw new IllegalArgumentException("Invalid VerificationTypeEnum value: " + value);
    }

    @Override
    public String toString() {
        return "VerifyProtocolType{" +
                "value=" + value +
                '}';
    }
}
