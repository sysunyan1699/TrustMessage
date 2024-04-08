package com.example.trustmessage.consumer.mq;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
public class RocketMQTransactionProducer {

    public static void main(String[] args) throws MQClientException {
        TransactionMQProducer producer = new TransactionMQProducer("producer_group");
        producer.setNamesrvAddr("localhost:9876"); // 设置NameServer地址

        // 注册事务监听器
        producer.setTransactionListener(new ExampleTransactionListener());

        // 启动生产者
        producer.start();

        String[] tags = new String[]{"TagA", "TagB", "TagC"};
        for (int i = 0; i < 3; i++) {
            Message msg = new Message("TopicTest1234", tags[i % tags.length], "KEY" + i,
                    ("Hello RocketMQ " + i).getBytes());
            // 发送事务消息
            // 注意：send方法可能会因为网络、超时等问题抛出异常
            producer.sendMessageInTransaction(msg, null);
        }

        // 注意：实际应用中，生产者应该根据需要继续运行，这里只是演示用途
        // 生产者不再发送消息时，关闭生产者实例
        // producer.shutdown();
    }
}
