package com.example.trustmessage.middleware.schedule;

import com.example.trustmessage.middleware.common.MessageSendStatus;
import com.example.trustmessage.middleware.mapper.MessageMapper;
import com.example.trustmessage.middleware.model.Message;
import com.example.trustmessage.middleware.service.InnerMessageService;
import com.example.trustmessage.middleware.utils.MessageUtils;
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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@EnableScheduling
@Component
public class SendMessageSchedule {
    private static final Logger logger = LoggerFactory.getLogger(SendMessageSchedule.class);

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private InnerMessageService innerMessageService;
    @Value("${send.maxTryCount:15}")
    private int sendMaxTryCount;
    @Value("${send.tryPeriod:600}")
    private int sendTryPeriod;
    @Value("${send.selectLimitCount:100}")
    private int sendSelectLimitCount;
    ThreadPoolExecutor executor = new ThreadPoolExecutor(
            16,
            32,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(500),
            new ThreadPoolExecutor.AbortPolicy()
    );

    @Scheduled(fixedRate = 60000)
    public void verifyMessageScheduledTask() {
        logger.info("verifyMessageScheduledTask executed at:{} ", new java.util.Date());
        int minID = 0;
        while (true) {
            Map<String, Object> params = new HashMap<>();
            params.put("id", minID);
            params.put("limitCount", sendSelectLimitCount);
            List<Message> messageList = messageMapper.findMessagesForSend(params);
            if (messageList.size() == 0) {
                logger.info("verifyMessageScheduledTask at:{} ", new java.util.Date());
                return;
            }
            for (Message m : messageList) {
                minID = Math.max(minID, m.getId());
                doSend(m);
                //executor.execute(new SendMessage(m));
            }
            if (messageList.size() < sendSelectLimitCount) {
                logger.info("verifyMessageScheduledTask finish at:{} ", new java.util.Date());
                return;
            }
        }
    }


    private void doSend(Message m) {
        // 处理发送重试已达最大次数，但是状态更新失败的数据,打印错误日志告警
        if (m.getSendTryCount() >= sendMaxTryCount) {
            logger.error("发送重试已达最大次数，bizId:{},messageKey:{},message:{}",
                    m.getBizID(), m.getMessageKey(), m.getMessage());
            innerMessageService.updateSendStatusByMessageKeyAndBizID(m.getBizID(),
                    m.getMessageKey(),
                    MessageSendStatus.NOT_SEND.getValue(),
                    MessageSendStatus.SEND_FAIL.getValue()
            );
        }
        //调用kafka 发送消息, 根据消息发送结果进行业务处理
        boolean send = doSendMessage(m);
        if (send) {
            // 发送成功的消息不再维护 下次发送重试时间
            // 如果状态更新失败，此时未更新重试信息，下次定时依然会把这条消息找出来走到该逻辑，所以消息消费者要做好幂等
            innerMessageService.updateSendInfo(
                    m.getBizID(),
                    m.getMessageKey(),
                    MessageSendStatus.NOT_SEND.getValue(),
                    MessageSendStatus.SEND_SUCCESS.getValue(),
                    m.getSendTryCount() + 1,
                    null);

        } else {

            //  重试达到最大次数， 不再重试，告警处理
            if (m.getSendTryCount() + 1 == sendMaxTryCount) {
                logger.error("发送重试已达最大次数，bizId:{},messageKey:{},message:{}",
                        m.getBizID(), m.getMessageKey(), m.getMessage());

                // 这里如果更新失败了，会继续重试
                innerMessageService.updateSendInfo(
                        m.getBizID(),
                        m.getMessageKey(),
                        MessageSendStatus.NOT_SEND.getValue(),
                        MessageSendStatus.SEND_FAIL.getValue(),
                        m.getSendTryCount() + 1,
                        null);
            } else {
                int plusSeconds = MessageUtils.GetSendNextRetryTimeSeconds(m.getSendTryCount());

                // 更新重试信息
                innerMessageService.updateSendRetryCountAndTime(
                        m.getBizID(),
                        m.getMessageKey(),
                        m.getMessageStatus(),
                        m.getSendTryCount() + 1,
                        LocalDateTime.now().plusSeconds(plusSeconds));
            }

        }

        class SendMessage implements Runnable {
            private Message m;

            public SendMessage(Message m) {
                this.m = m;
            }
            @Override
            public void run() {
                doSend(m);
            }
        }
    }

    private boolean doSendMessage(Message m) {
        return false;
    }

}
