package com.example.trustmessage.middleware.schedule;

import com.example.trustmessage.middleware.utils.JsonUtil;
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
        logger.info("verifyScheduledTask executed at: {}", new java.util.Date());
        int minID = 0;
        while (true) {
            Map<String, Object> params = new HashMap<>();
            params.put("id", minID);
            params.put("limitCount", verifySelectLimitCount);
            List<Message> messageList = messageMapper.findMessagesForVerify(params);
            if (messageList.size() == 0) {
                logger.info("verifyScheduledTask finish  at: {}", new java.util.Date());
                return;
            }
            for (Message m : messageList) {
                minID = Math.max(minID, m.getId());
                doVerify(m);
                //executor.execute(new VerifyMessage(m));
            }
            if (messageList.size() < verifySelectLimitCount) {
                logger.info("verifyScheduledTask finish  at: {}", new java.util.Date());
                return;
            }
        }

    }


    public void doVerify(Message m) {
        // 防止之前状态修改有遗漏
        if (m.getVerifyTryCount() >= verifyMaxTryCount) {
            logger.error("状态回查重试已达最大次数，bizID:{},messageKey:{},message:{}",
                    m.getBizID(), m.getMessageKey(), m.getMessage());
            messageService.updateMessageStatusByMessageKeyAndBizID(
                    m.getBizID(),
                    m.getMessageKey(),
                    MessageStatus.PREPARE.getValue(),
                    MessageStatus.VERIFY_FAIL.getValue()
            );
            return;
        }
        try {
            MiddlewareMessage.VerifyInfo verifyInfo = JsonUtil.readValue(m.getVerifyInfo(), MiddlewareMessage.VerifyInfo.class);
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
                                MessageStatus.PREPARE.getValue(),
                                MessageStatus.VERIFY_FAIL.getValue()
                        );
                    } else {
                        messageService.updateVerifyRetryCountAndTime(
                                m.getBizID(),
                                m.getMessageKey(),
                                m.getMessageStatus(),
                                m.getVerifyTryCount() + 1,
                                LocalDateTime.now().plusSeconds(MessageUtils.GetVerifyNextRetryTimeSeconds(m.getVerifyTryCount() + 1)));
                    }
                    break;
                case COMMIT:
                    // 直接修改状态, 等待消息发送定时任务将消息发送至下游
                    messageService.updateVerifyInfo(
                            m.getBizID(),
                            m.getMessageKey(),
                            MessageStatus.PREPARE.getValue(),
                            MessageStatus.COMMIT.getValue(),
                            m.getVerifyTryCount() + 1,
                            null);

                case ROLLBACK:
                    // 直接修改状态
                    messageService.updateVerifyInfo(
                            m.getBizID(),
                            m.getMessageKey(),
                            MessageStatus.PREPARE.getValue(),
                            MessageStatus.ROLLBACK.getValue(),
                            m.getVerifyTryCount() + 1,
                            null);
                    break;
            }
        } catch (JsonProcessingException e) {
            logger.error("verifyScheduledTask doVerify error, message:{}, error:{}", m, e);
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
