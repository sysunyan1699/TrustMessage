package com.example.trustmessage.middleware.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Message {

    private int id;
    // 要给到业务方的真正消息

    //业务线区分
    private int bizID;
    private String message;

    // 用于消息回查的业务唯一标识
    private String messageKey;


    private int messageStatus;

    // 需要发送至的真正消息队列topic
    private String forwardTopic;

    // 向业务方转发时需要指定的key，没有则说明按照kafka 默认分区策略进行分区
    private String forwardKey;

    private String verifyInfo;

    //当前回查重试次数
    private int verifyTryCount;
    //下一次进行消息回查的重试时间
    private LocalDateTime verifyNextRetryTime;

    // 1-未发送 2-已发送
    private int sendStatus;
    // 当前消息尝试发送的次数
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


    public int getBizID() {
        return bizID;
    }

    public void setBizID(int bizID) {
        this.bizID = bizID;
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

    public String getForwardTopic() {
        return forwardTopic;
    }

    public void setForwardTopic(String forwardTopic) {
        this.forwardTopic = forwardTopic;
    }

    public String getForwardKey() {
        return forwardKey;
    }

    public void setForwardKey(String forwardKey) {
        this.forwardKey = forwardKey;
    }

    public String getVerifyInfo() {
        return verifyInfo;
    }

    public void setVerifyInfo(String verifyInfo) {
        this.verifyInfo = verifyInfo;
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
                ", bizID=" + bizID +
                ", message='" + message + '\'' +
                ", messageKey='" + messageKey + '\'' +
                ", messageStatus=" + messageStatus +
                ", forwardTopic='" + forwardTopic + '\'' +
                ", forwardKey='" + forwardKey + '\'' +
                ", verifyInfo='" + verifyInfo + '\'' +
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
        return id == message1.id && bizID == message1.bizID && messageStatus == message1.messageStatus && verifyTryCount == message1.verifyTryCount && sendStatus == message1.sendStatus && sendTryCount == message1.sendTryCount && Objects.equals(message, message1.message) && Objects.equals(messageKey, message1.messageKey) && Objects.equals(forwardTopic, message1.forwardTopic) && Objects.equals(forwardKey, message1.forwardKey) && Objects.equals(verifyInfo, message1.verifyInfo) && Objects.equals(verifyNextRetryTime, message1.verifyNextRetryTime) && Objects.equals(sendNextRetryTime, message1.sendNextRetryTime) && Objects.equals(createTime, message1.createTime) && Objects.equals(updateTime, message1.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bizID, message, messageKey, messageStatus, forwardTopic, forwardKey, verifyInfo, verifyTryCount, verifyNextRetryTime, sendStatus, sendTryCount, sendNextRetryTime, createTime, updateTime);
    }
}
