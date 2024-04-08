package com.example.trustmessage.consumer.mapper;


import com.example.trustmessage.consumer.model.MessageStore;

import java.util.Map;
import  org.apache.ibatis.annotations.Param;


public interface MessageStoreMapper {
    /**
     * 插入一条消息记录
     *
     * @param messageStore 消息存储对象
     */
    void insertMessage(MessageStore messageStore);

    /**
     * 根据key查找一条消息记录
     *
     * @param key 消息的唯一标识
     * @return 消息存储对象
     */
    MessageStore findByKey(@Param("key") String key);

    /**
     * 根据key更新消息的状态
     *
     * @param params 包含key和要更新的状态
     */
    void updateStatusByKey(@Param("params") Map<String, Object> params);
}
