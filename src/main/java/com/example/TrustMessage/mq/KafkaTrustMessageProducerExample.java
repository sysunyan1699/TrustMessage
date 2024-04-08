package com.example.trustmessage.consumer.mq;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.apache.kafka.common.serialization.StringSerializer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class KafkaTrustMessageProducerExample {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", StringSerializer.class.getName());

        try (Producer<String, String> producer = new KafkaProducer<>(props)) {
            String topic = "your-topic-name";

            Headers headers = new RecordHeaders()
                    .add("MessageType", "prepare".getBytes(StandardCharsets.UTF_8))
                    .add("BusinessTopic", "business-topic".getBytes(StandardCharsets.UTF_8));

            ProducerRecord<String, String> record = new ProducerRecord<>(topic, null, "your-key", "your-message", headers);
            producer.send(record);

            System.out.println("Message sent successfully");
        }
    }
}

