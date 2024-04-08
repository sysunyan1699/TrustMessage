package com.example.trustmessage.consumer.model;

import com.example.trustmessage.common.MiddlewareMessage;

public class MessageStore {
    //业务 topic
    private String forwardTopic;

    //消息的唯一标识
    private String key;

    //消息状态
    private int status;

    //消息体
    private String payload;
    private int protocolType;
    private String endpoint;
    private String method; // 注意：对于非HTTP协议，这个字段的意义可能不同或不需要
}
