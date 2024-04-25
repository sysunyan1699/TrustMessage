package com.example.trustmessage.mysql.service;

import com.example.trustmessage.mysql.common.MessageStatus;

public interface BusinessService {

    void business();

    MessageStatus verifyMessageStatus(String messageKey);
}
