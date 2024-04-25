package com.example.trustmessage.middleware.schedule;

import com.example.trustmessage.middlewareapi.common.MessageStatus;
import com.example.trustmessage.middlewareapi.common.MiddlewareMessage;
import com.example.trustmessage.middleware.mapper.MessageMapper;
import com.example.trustmessage.middleware.model.Message;
import com.example.trustmessage.middleware.service.GenericVerifyService;
import com.example.trustmessage.middleware.service.InnerMessageService;
import com.example.trustmessage.middleware.service.VerifyServiceFactory;
import com.example.trustmessage.middleware.utils.MessageUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@EnableScheduling
@Component
public class VerifyMessageSchedule {

    private static final Logger logger = LoggerFactory.getLogger(VerifyMessageSchedule.class);

    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private VerifyServiceFactory verifyServiceFactory;

    @Autowired
    private InnerMessageService messageService;
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
    public void verifyScheduledTask() {
        logger.info("Task executed at: {}", new java.util.Date());
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
                logger.info("Task finish  at: {}", new java.util.Date());
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
            logger.info("doVerify, bizID:{}, messageKey:{}, verifyResult:{}", m.getBizID(), m.getMessageKey(), status);
            switch (MessageStatus.valueOf(status)) {
                case PREPARE:
                    // 此次重试后达到重试最大次数后，不再重试，直接修改状态为rollback, 并且标识次rollback 状态是最终重试失败后造成的
                    if (m.getVerifyTryCount() == verifyMaxTryCount) {
                        //配置告警机制，进行告警
                        logger.error("doVerify maxTime, bizID:{}, messageKey:{} ", m.getBizID(), m.getMessageKey());
                        messageService.updateMessageStatusByMessageKeyAndBizID(
                                m.getBizID(),
                                m.getMessageKey(),
                                MessageStatus.UNKNOWN.getValue(),
                                MessageStatus.PREPARE.getValue());
                    } else {
                        int plusSeconds = MessageUtils.GetVerifyNextRetryTimeSeconds(m.getVerifyTryCount());
                        messageService.updateVerifyRetryCountAndTime(
                                m.getBizID(),
                                m.getMessageKey(),
                                m.getVerifyTryCount() + 1,
                                LocalDateTime.now().plusSeconds(plusSeconds));
                    }
                    break;
                case COMMIT:
                    // 直接修改状态, 等待消息发送定时任务将消息发送至下游
                    messageService.updateMessageStatusByMessageKeyAndBizID(
                            m.getBizID(),
                            m.getMessageKey(),
                            status,
                            m.getMessageStatus());

                case ROLLBACK:
                    // 直接修改状态
                    messageService.updateMessageStatusByMessageKeyAndBizID(
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
            doVerify(m);
        }
    }

}
