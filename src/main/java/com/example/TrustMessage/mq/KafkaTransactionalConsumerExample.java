package com.example.trustmessage.consumer.mq;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class KafkaTransactionalConsumerExample {
    public static void main(String[] args) {
        // 设置消费者属性
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "your_kafka_broker:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "your_consumer_group");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        // 设置为 read_committed 以确保只消费已经提交的事务消息
        properties.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties)) {
            // 订阅主题
            consumer.subscribe(Collections.singletonList("your_topic"));

            while (true) {
                // 轮询新的消息
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("Consumed record with key %s and value %s%n", record.key(), record.value());
                    // 在这里实现你的消息处理逻辑
                }

                // 手动提交偏移量
                // 注意：在实际应用中，你可能需要根据你的业务逻辑来决定是否和何时提交偏移量
                consumer.commitSync();
            }
        }
    }
}

