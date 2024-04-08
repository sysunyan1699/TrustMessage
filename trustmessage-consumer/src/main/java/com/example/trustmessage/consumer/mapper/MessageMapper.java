package com.example.trustmessage.consumer.mapper;


import com.example.trustmessage.consumer.model.Message;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageMapper {
    /**
     * 插入一条消息记录
     *
     * @param message 消息存储对象
     */
    int insertMessage(Message message);

    /**
     * 根据key查找一条消息记录
     *
     * @return 消息存储对象
     */
    Message findByMessageKeyAndBizID(Map<String, Object> params);

    /**
     * 根据key更新消息的状态
     *
     * @param params 包含key和要更新的状态
     */
    int updateStatusByMessageKeyAndBizID(Map<String, Object> params);

    int updateRetryCountAndTime(Map<String, Object> params);


    List<Message> findMessagesForVerify(Map<String, Object> params);


}
