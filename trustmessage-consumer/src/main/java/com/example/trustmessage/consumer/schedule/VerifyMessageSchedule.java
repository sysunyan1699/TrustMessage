package com.example.trustmessage.consumer.schedule;

import com.example.trustmessage.common.MessageStatus;
import com.example.trustmessage.common.MiddlewareMessage;
import com.example.trustmessage.consumer.mapper.MessageMapper;
import com.example.trustmessage.consumer.model.Message;
import com.example.trustmessage.consumer.service.GenericVerifyService;
import com.example.trustmessage.consumer.service.MessageService;
import com.example.trustmessage.consumer.service.VerifyServiceFactory;
import com.example.trustmessage.consumer.utils.MessageUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@EnableScheduling
@Component
public class VerifyMessageSchedule {

    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private VerifyServiceFactory verifyServiceFactory;

    @Autowired
    private MessageService messageService;
    @Value("${verify.maxTryCount:15}")
    private int verifyMaxTryCount;
    @Value("${verify.tryPeriod:600}")
    private int verifyTryPeriod;

    @Value("${verify.selectLimitCount:100}")
    private int verifySelectLimitCount;

    int corePoolSize = Runtime.getRuntime().availableProcessors() * 2; // 对IO密集型任务，可以增加核心线程数
    int maximumPoolSize = corePoolSize * 2; // 最大线程数可以设置更大
    long keepAliveTime = 60L; // 增加闲置超时时间，给予线程更多的等待时间，减少创建和销毁的开销
    TimeUnit unit = TimeUnit.SECONDS;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(500); // 根据任务队列的实际情况调整
    ThreadPoolExecutor executor = new ThreadPoolExecutor(
            corePoolSize,
            maximumPoolSize,
            keepAliveTime,
            unit,
            workQueue,
            new ThreadPoolExecutor.CallerRunsPolicy() // 饱和策略
    );

    @Scheduled(fixedRate = 60000)
    public void scheduledTask() {
        System.out.println("Task executed at: " + new java.util.Date());
        int minID = 0;
        while (true) {
            Map<String, Object> params = new HashMap<>();
            params.put("id", minID);
            params.put("limitCount", verifySelectLimitCount);
            List<Message> messageList = messageMapper.findMessagesForVerify(params);
            if (messageList.size() == 0) {
                return;
            }
            for (Message m : messageList) {
                minID = Math.max(minID, m.getId());
                doVerify(m);
                //executor.execute(new VerifyMessage(m));
            }

            if (messageList.size() < verifySelectLimitCount) {
                return;
            }
        }

    }


    public void doVerify(Message m) {
        if (m.getMessageStatus() != MessageStatus.PREPARE.getValue()) {
            return;
        }
        MiddlewareMessage.VerifyInfo verifyInfo;
        try {
            ObjectMapper mapper = new ObjectMapper();
            verifyInfo = mapper.readValue(m.getVerifyInfo(), MiddlewareMessage.VerifyInfo.class);
            GenericVerifyService genericVerifyService = verifyServiceFactory.getVerifyService(verifyInfo.getProtocolType());
            int status = genericVerifyService.invoke(m.getBizID(), m.getMessageKey(), verifyInfo);
            // 根据结果，如果是prepare 则不需要处理状态只更新重试相关信息，如果是commit 或者rollback 更新状态和重试信息

            switch (MessageStatus.valueOf(status)) {
                case PREPARE:
                    // 此次重试后达到重试最大次数后，不再重试， 直接修改状态为rollback, 并且标识次rollback 状态是最终重试失败后造成的
                    if (m.getTryCount() == verifyMaxTryCount) {
                        messageService.updateStatusByMessageKeyAndBizID(
                                m.getBizID(),
                                m.getMessageKey(),
                                MessageStatus.RETRY_ROLLBACK.getValue(),
                                MessageStatus.PREPARE.getValue());
                    } else {
                        MessageUtils.FillRetryInformation(m, m.getTryCount() + 1);
                        messageService.updateRetryCountAndTime(m.getBizID(),
                                m.getMessageKey(),
                                m.getTryCount(),
                                m.getNextRetryTime());
                    }
                    break;
                case COMMIT:
                    // 直接修改状态
                    messageService.updateStatusByMessageKeyAndBizID(
                            m.getBizID(),
                            m.getMessageKey(),
                            status,
                            m.getMessageStatus());
                    // todo 将对应的消息转发至真正的业务消息队列， 此处需要本地事务+本地消息表？？

                case ROLLBACK:
                    // 直接修改状态
                    messageService.updateStatusByMessageKeyAndBizID(
                            m.getBizID(),
                            m.getMessageKey(),
                            status,
                            m.getMessageStatus());
                    break;
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    class VerifyMessage implements Runnable {
        private Message m;

        public VerifyMessage(Message m) {
            this.m = m;
        }

        @Override
        public void run() {
            if (m.getMessageStatus() != MessageStatus.PREPARE.getValue()) {
                return;
            }
            MiddlewareMessage.VerifyInfo verifyInfo;
            try {
                ObjectMapper mapper = new ObjectMapper();
                verifyInfo = mapper.readValue(m.getVerifyInfo(), MiddlewareMessage.VerifyInfo.class);
                GenericVerifyService genericVerifyService = verifyServiceFactory.getVerifyService(verifyInfo.getProtocolType());
                int status = genericVerifyService.invoke(m.getBizID(), m.getMessageKey(), verifyInfo);
                // 根据结果，如果是prepare 则不需要处理状态只更新重试相关信息，如果是commit 或者rollback 更新状态和重试信息

                switch (MessageStatus.valueOf(status)) {
                    case PREPARE:
                        // 此次重试后达到重试最大次数后，不再重试， 直接修改状态为rollback, 并且标识次rollback 状态是最终重试失败后造成的
                        if (m.getTryCount() == verifyMaxTryCount) {
                            messageService.updateStatusByMessageKeyAndBizID(
                                    m.getBizID(),
                                    m.getMessageKey(),
                                    MessageStatus.RETRY_ROLLBACK.getValue(),
                                    MessageStatus.PREPARE.getValue());
                        } else {
                            MessageUtils.FillRetryInformation(m, m.getTryCount() + 1);
                            messageService.updateRetryCountAndTime(m.getBizID(),
                                    m.getMessageKey(),
                                    m.getTryCount(),
                                    m.getNextRetryTime());
                        }
                        break;
                    case COMMIT:

                    case ROLLBACK:
                        // 直接修改状态
                        messageService.updateStatusByMessageKeyAndBizID(
                                m.getBizID(),
                                m.getMessageKey(),
                                status,
                                m.getMessageStatus());
                        break;
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

}


//查处本地定时扫描到⌚️的消息，根据提供的回查消息进行回查
//int offset = 0;


//一次取200条， 放在线程池执行，失败了没关系，下次还会扫到。
// 即使没有按照规定的回查时间段进行立马回查也没有关系，毕竟业务过了那么久都没来commit或者rollback, 你晚一点肯定也没关系
// 如果该服务部署成集群模式， 就需要外部定时器来发起任务， 不能每台机器都各自任意执行
//消息的重试次数和重试间隔有上限，rugu
