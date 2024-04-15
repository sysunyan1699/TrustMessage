package com.example.trustmessage.mysql.mapper;


import java.util.List;
import java.util.Map;

import com.example.trustmessage.mysql.model.Message;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageMapper {
    /**
     * 插入一条消息记录
     *
     * @param message 消息存储对象
     */
    int insertMessage(Message message);

    Message findByMessageKey(Map<String, Object> params);

    int updateMessageStatusByMessageKey(Map<String, Object> params);

    int updateSendStatusByMessageKey(Map<String, Object> params);

    int updateVerifyRetryCountAndTime(Map<String, Object> params);

    int updateSendRetryCountAndTime(Map<String, Object> params);

    List<Message> findMessagesForVerify(Map<String, Object> params);

    List<Message> findMessagesForSend(Map<String, Object> params);


}
