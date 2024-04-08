package com.example.trustmessage.consumer.mq;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaTransactionalProducerExample {
    public static void main(String[] args) {
        // 设置生产者属性
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "your_kafka_broker:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // 开启幂等性
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        // 配置事务ID
        properties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "transactional_id_example");

        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        // 初始化事务
        producer.initTransactions();

        try {
            // 开始事务
            producer.beginTransaction();

            // 发送消息
            for (int i = 0; i < 10; i++) {
                producer.send(new ProducerRecord<>("your_topic", Integer.toString(i), "message " + i));
            }

            // 模拟业务逻辑处理
            // 根据业务逻辑处理的结果，决定是提交事务还是中止事务
            boolean businessLogicSuccess = true; // 假设业务逻辑执行成功

            if (businessLogicSuccess) {
                // 提交事务
                producer.commitTransaction();
            } else {
                // 中止事务
                producer.abortTransaction();
            }
        } catch (Exception e) {
            // 异常处理逻辑
            producer.abortTransaction();
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }
}
