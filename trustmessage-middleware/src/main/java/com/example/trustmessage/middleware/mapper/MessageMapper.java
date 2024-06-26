package com.example.trustmessage.middleware.mapper;

import com.example.trustmessage.middleware.model.Message;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageMapper {

    int insertMessage(Message message);

    Message findByMessageKeyAndBizID(Map<String, Object> params);

    List<Message> findMessagesForVerify(Map<String, Object> params);

    List<Message> findMessagesForSend(Map<String, Object> params);

    int updateSendInfo(Map<String, Object> params);

    int updateVerifyInfo(Map<String, Object> params);

}
