package com.example.trustmessage.middleware.utils;

import com.example.trustmessage.middleware.service.impl.MessageServiceImpl;
import com.example.trustmessage.middlewareapi.common.MiddlewareMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtil {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);


    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toJson(Object data) throws JsonProcessingException {
        return mapper.writeValueAsString(data);  // 直接抛出异常，让调用者处理
    }

    public static <T> T readValue(String jsonStr, Class<T> valueType) throws JsonProcessingException {
        return mapper.readValue(jsonStr, valueType);  // 同样，直接抛出异常
    }
}
