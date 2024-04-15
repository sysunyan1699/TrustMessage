package com.example.trustmessage.mysql.schedule;


import com.example.trustmessage.mysql.common.MessageStatus;
import com.example.trustmessage.mysql.mapper.MessageMapper;
import com.example.trustmessage.mysql.model.Message;
import com.example.trustmessage.mysql.service.BusinessService;
import com.example.trustmessage.mysql.service.MessageService;
import com.example.trustmessage.mysql.utils.MessageUtils;
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
    private BusinessService businessService;

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


    private void doVerify(Message m) {
        if (m.getMessageStatus() != MessageStatus.PREPARE.getValue()) {
            return;
        }
        // 防止之前状态修改有遗漏
        if (m.getVerifyTryCount() == verifyMaxTryCount) {
            messageService.updateMessageStatusByMessageKey(
                    m.getMessageKey(),
                    MessageStatus.UNKNOWN.getValue(),
                    MessageStatus.PREPARE.getValue());

            //todo 打印错误日志，进行告警
            return;
        }
        MessageStatus verifyResult = businessService.verifyMessageStatus(m.getMessageKey());
        switch (verifyResult) {
            case PREPARE:
                // 此次重试已经是最大重试次数，之后不再重试， 直接修改状态为unknown 并告警人工介入
                logger.info("doVerify, bizID:{}, messageKey:{}, verifyResult:{}", m.getMessageKey(), verifyResult);
                if (m.getVerifyTryCount() == verifyMaxTryCount) {
                    messageService.updateMessageStatusByMessageKey(
                            m.getMessageKey(),
                            MessageStatus.UNKNOWN.getValue(),
                            MessageStatus.PREPARE.getValue());
                    // 打印错误日志，进行告警
                } else {
                    messageService.updateVerifyRetryCountAndTime(
                            m.getMessageKey(),
                            m.getVerifyTryCount() + 1,
                            LocalDateTime.now().plusSeconds(MessageUtils.GetVerifyNextRetryTimeSeconds(m.getVerifyTryCount())));
                }
                break;
            case COMMIT:
                // 直接修改状态
                messageService.commitMessage(m.getMessageKey());
                break;
            case ROLLBACK:
                messageService.rollbackMessage(m.getMessageKey());
                break;
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
