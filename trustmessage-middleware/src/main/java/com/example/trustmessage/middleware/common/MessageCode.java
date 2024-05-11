package com.example.trustmessage.middleware.common;

// client 提交消息，返回的code码
public enum MessageCode {

    SUCCESS(200, "success"),
    ILLEGAL_PARAM(1000, "参数不合法"),
    MESSAGE_NOT_EXIST(1001, "消息不存在"),
    MESSAGE_COMMITED(1002, "消息已提交，无法回滚"),
    MESSAGE_ROLLBACKED(1003, "消息已回滚，无法提交");



    private int value;

    private String des;

    MessageCode(int value, String des) {
        this.value = value;
        this.des = des;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
