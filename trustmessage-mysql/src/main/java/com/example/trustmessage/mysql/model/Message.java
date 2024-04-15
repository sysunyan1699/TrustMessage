package com.example.trustmessage.mysql.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Message {

    private int id;
    // 要给到业务方的真正消息
    private String message;

    // 用于消息回查的业务唯一标识
    private String messageKey;

    private int messageStatus;

    //下次查询的回查次数， 初始值为1
    private int verifyTryCount;
    //下一次进行消息回查的重试时间，初始值为第一次需要查询的时间
    private LocalDateTime verifyNextRetryTime;

    // 1-未发送 2-已发送
    private int sendStatus;
    // 下次消息尝试发送的次数，初始值为1
    private int sendTryCount;
    // 下一次消息尝试发送的时间
    private LocalDateTime sendNextRetryTime;


    private LocalDateTime createTime;
    private LocalDateTime updateTime;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public int getVerifyTryCount() {
        return verifyTryCount;
    }

    public void setVerifyTryCount(int verifyTryCount) {
        this.verifyTryCount = verifyTryCount;
    }

    public LocalDateTime getVerifyNextRetryTime() {
        return verifyNextRetryTime;
    }

    public void setVerifyNextRetryTime(LocalDateTime verifyNextRetryTime) {
        this.verifyNextRetryTime = verifyNextRetryTime;
    }

    public int getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(int sendStatus) {
        this.sendStatus = sendStatus;
    }

    public int getSendTryCount() {
        return sendTryCount;
    }

    public void setSendTryCount(int sendTryCount) {
        this.sendTryCount = sendTryCount;
    }

    public LocalDateTime getSendNextRetryTime() {
        return sendNextRetryTime;
    }

    public void setSendNextRetryTime(LocalDateTime sendNextRetryTime) {
        this.sendNextRetryTime = sendNextRetryTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }


    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", messageKey='" + messageKey + '\'' +
                ", messageStatus=" + messageStatus +
                ", verifyTryCount=" + verifyTryCount +
                ", verifyNextRetryTime=" + verifyNextRetryTime +
                ", sendStatus=" + sendStatus +
                ", sendTryCount=" + sendTryCount +
                ", sendNextRetryTime=" + sendNextRetryTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return id == message1.id && messageStatus == message1.messageStatus && verifyTryCount == message1.verifyTryCount && sendStatus == message1.sendStatus && sendTryCount == message1.sendTryCount && Objects.equals(message, message1.message) && Objects.equals(messageKey, message1.messageKey) && Objects.equals(verifyNextRetryTime, message1.verifyNextRetryTime) && Objects.equals(sendNextRetryTime, message1.sendNextRetryTime) && Objects.equals(createTime, message1.createTime) && Objects.equals(updateTime, message1.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, message, messageKey, messageStatus, verifyTryCount, verifyNextRetryTime, sendStatus, sendTryCount, sendNextRetryTime, createTime, updateTime);
    }
}
