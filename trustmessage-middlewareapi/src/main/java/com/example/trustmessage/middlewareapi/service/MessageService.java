package com.example.trustmessage.middlewareapi.service;

import com.example.trustmessage.middlewareapi.common.MessageResponse;
import com.example.trustmessage.middlewareapi.common.MiddlewareMessage;

public interface MessageService {

    MessageResponse prepareMessage(MiddlewareMessage m);

    MessageResponse commitMessage(MiddlewareMessage m);

    MessageResponse rollBackMessage(MiddlewareMessage m);


}
