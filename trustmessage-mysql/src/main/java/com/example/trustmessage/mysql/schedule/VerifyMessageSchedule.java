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

    ThreadPoolExecutor executor = new ThreadPoolExecutor(
            16,
            32,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(500),
            new ThreadPoolExecutor.AbortPolicy()
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
                logger.info("Task finish  at: {}", new java.util.Date());
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
        // 防止之前状态修改有遗漏
        if (m.getVerifyTryCount() >= verifyMaxTryCount) {
            logger.error("状态回查重试已达最大次数，messageKey:{},message:{}",
                    m.getMessageKey(), m.getMessage());
            messageService.updateMessageStatusByMessageKey(
                    m.getMessageKey(),
                    MessageStatus.VERIFY_FAIL.getValue(),
                    MessageStatus.PREPARE.getValue());

            return;
        }
        // 消息状态回查
        MessageStatus verifyResult = businessService.verifyMessageStatus(m.getMessageKey());
        switch (verifyResult) {
            case PREPARE:
                // 此次重试已经是最大重试次数，打印错误日志告警
                if (m.getVerifyTryCount() + 1 == verifyMaxTryCount) {
                    logger.error("消息回查重试已达最大次数, messageKey:{}, message:{}", m.getMessageKey(), m.getMessage());
                    messageService.updateVerifyInfo(
                            m.getMessageKey(),
                            MessageStatus.VERIFY_FAIL.getValue(),
                            MessageStatus.PREPARE.getValue(),
                            m.getVerifyTryCount() + 1,
                            null);
                } else {
                    messageService.updateVerifyRetryCountAndTime(
                            m.getMessageKey(),
                            m.getVerifyTryCount() + 1,
                            LocalDateTime.now().plusSeconds(MessageUtils.GetVerifyNextRetryTimeSeconds(m.getVerifyTryCount())));
                }
                break;
            case COMMIT:
                // 直接修改状态
                messageService.updateVerifyInfo(
                        m.getMessageKey(),
                        MessageStatus.COMMIT.getValue(),
                        MessageStatus.PREPARE.getValue(),
                        m.getVerifyTryCount() + 1,
                        null);
                break;
            case ROLLBACK:
                messageService.updateVerifyInfo(
                        m.getMessageKey(),
                        MessageStatus.ROLLBACK.getValue(),
                        MessageStatus.PREPARE.getValue(),
                        m.getVerifyTryCount() + 1,
                        null);
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
