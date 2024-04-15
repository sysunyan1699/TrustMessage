package com.example.trustmessage.mysql.service;

import com.example.trustmessage.mysql.common.MessageStatus;

public interface BusinessService {

    public void business();

    public MessageStatus verifyMessageStatus(String messageKey);
}
