package com.example.trustmessage.consumer.mq;

import com.example.trustmessage.consumer.mapper.MessageStoreMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

public class KafkaConsumer {

    @Autowired
    private MessageStoreMapper messageStoreMapper;

    //在 Kafka 中，每条消息都是一个 ConsumerRecord 类型，
    // 包含了消息的 key、value（消息体）、partition、offset 等信息。
    // 如果你想在 @KafkaListener 注解的方法中获取除了消息体以外的其他信息（如 key），
    // 你可以通过更改方法的参数来接收整个 ConsumerRecord 对象，而不是仅接收消息体。
    @KafkaListener(topics = "yourTopic", groupId = "my-group")
    public void listen(ConsumerRecord<String, String> record) {

        String key = record.key();
        String message = record.value();
        messageStoreMapper.findByKey(key);
        // 处理消息...
    }
}
