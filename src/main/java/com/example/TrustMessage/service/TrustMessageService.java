package com.example.trustmessage.consumer.service;

import com.example.trustmessage.consumer.model.MessageStore;

public interface TrustMessageService {

    boolean handlePrepareMessage(MessageStore messageStore);
    boolean handleCommitMessage(String key);
    boolean handleRollbackMessage(String key);

}
