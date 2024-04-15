package com.example.trustmessage.mysql.schedule;

import com.example.trustmessage.mysql.common.MessageSendStatus;
import com.example.trustmessage.mysql.common.MessageStatus;
import com.example.trustmessage.mysql.mapper.MessageMapper;
import com.example.trustmessage.mysql.model.Message;
import com.example.trustmessage.mysql.service.BusinessService;
import com.example.trustmessage.mysql.service.MessageService;
import com.example.trustmessage.mysql.utils.MessageUtils;
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
    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private MessageService messageService;
    @Value("${send.maxTryCount:15}")
    private int sendMaxTryCount;
    @Value("${send.tryPeriod:600}")
    private int sendTryPeriod;
    @Value("${send.selectLimitCount:100}")
    private int sendSelectLimitCount;

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
    public void sendMessageScheduledTask() {
        System.out.println("Task executed at: " + new java.util.Date());
        int minID = 0;
        while (true) {
            Map<String, Object> params = new HashMap<>();
            params.put("id", minID);
            params.put("limitCount", sendSelectLimitCount);
            List<Message> messageList = messageMapper.findMessagesForSend(params);
            if (messageList.size() == 0) {
                return;
            }
            for (Message m : messageList) {
                minID = Math.max(minID, m.getId());
                doSend(m);
                //executor.execute(new SendMessage(m));
            }
            if (messageList.size() < sendSelectLimitCount) {
                return;
            }
        }
    }


    private void doSend(Message m) {
        //todo  调用kafka 发送消息, 根据消息发送结果进行业务处理
        boolean send = false;
        if (send) {
            // 如果状态更新失败，此时未更新重试信息，下次定时依然会把这条消息找出来走到该逻辑，所以消息消费者要做好幂等
            messageService.updateSendStatusByMessageKey(
                    m.getMessageKey(),
                    MessageSendStatus.HAVE_SENDED.getValue(),
                    MessageSendStatus.NOT_SEND.getValue());

        } else {
            if (m.getSendTryCount() == sendMaxTryCount) {
                //  打印错误日志， 告警通知人工介入处理
                return;
            }
            int plusSeconds = MessageUtils.GetSendNextRetryTimeSeconds(m.getSendTryCount());

            // 更新重试信息
            messageService.updateSendRetryCountAndTime(m.getMessageKey(),
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
