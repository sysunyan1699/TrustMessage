package com.example.trustmessage.consumer.service;

import com.example.trustmessage.consumer.model.MessageStore;
import com.example.trustmessage.consumer.mapper.MessageStoreMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class TrustMessageServiceImpl implements TrustMessageService {
    @Autowired
    private MessageStoreMapper messageStoreMapper;

    @Override
    public boolean handlePrepareMessage(MessageStore messageStore) {


        return false;
    }

    @Override
    public boolean handleCommitMessage(String key) {
        return false;
    }

    @Override
    public boolean handleRollbackMessage(String key) {
        return false;
    }
}
