package com.example.trustmessage.middleware.utils;

import com.example.trustmessage.middleware.common.MessageCode;
import com.example.trustmessage.middlewareapi.common.MessageResponse;

public class MessageResponseUtil {


    public static MessageResponse getMessageResponse(MessageCode code, boolean result) {
        return new MessageResponse(code.getValue(), code.getDes(), new MessageResponse.Data(result));
    }
}
