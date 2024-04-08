package com.example.trustmessage.consumer.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Message {

    private int id;
    // 要给到业务方的真正消息
    private String message;

    // 用于消息回查的业务唯一标识
    private String messageKey;

    //业务线区分
    private int bizID;

    private int messageStatus;

    // 需要发送至的真正消息队列topic
    private String forwardTopic;

    // 向业务方转发时需要指定的key，没有则说明按照kafka 默认分区策略进行分区
    private String forwardKey;

    private String verifyInfo;


    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 下一次进行消息回查的重试次数
    private int tryCount;
    //下一次进行消息回查的重试时间
    private LocalDateTime nextRetryTime;


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

    public int getTryCount() {
        return tryCount;
    }

    public void setTryCount(int tryCount) {
        this.tryCount = tryCount;
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

    public LocalDateTime getNextRetryTime() {
        return nextRetryTime;
    }

    public void setNextRetryTime(LocalDateTime nextRetryTime) {
        this.nextRetryTime = nextRetryTime;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", messageKey='" + messageKey + '\'' +
                ", bizID=" + bizID +
                ", messageStatus=" + messageStatus +
                ", forwardTopic='" + forwardTopic + '\'' +
                ", forwardKey='" + forwardKey + '\'' +
                ", verifyInfo='" + verifyInfo + '\'' +
                ", tryCount=" + tryCount +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", nextRetryTime=" + nextRetryTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return id == message1.id && bizID == message1.bizID && messageStatus == message1.messageStatus && tryCount == message1.tryCount && nextRetryTime == message1.nextRetryTime && Objects.equals(message, message1.message) && Objects.equals(messageKey, message1.messageKey) && Objects.equals(forwardTopic, message1.forwardTopic) && Objects.equals(forwardKey, message1.forwardKey) && Objects.equals(verifyInfo, message1.verifyInfo) && Objects.equals(createTime, message1.createTime) && Objects.equals(updateTime, message1.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, message, messageKey, bizID, messageStatus, forwardTopic, forwardKey, verifyInfo);
    }



}
