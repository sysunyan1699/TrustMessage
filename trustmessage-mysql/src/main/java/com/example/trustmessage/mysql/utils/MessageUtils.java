package com.example.trustmessage.mysql.utils;


import com.example.trustmessage.mysql.model.Message;

import java.time.LocalDateTime;

public class MessageUtils {
    // 可以是其他的重试时间策略
    public static int getVerifyNextRetryTimeSeconds(int verifyTryCount) {
        return 60 * (verifyTryCount + 1);
    }

    public static int getSendNextRetryTimeSeconds(int sendTryCount) {
        return 60 * (sendTryCount + 1);
    }
}
