package com.example.trustmessage.middleware.utils;

import com.example.trustmessage.middleware.service.impl.MessageServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtil {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);


    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toJson(Object data) throws JsonProcessingException {
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            // 日志记录异常
            logger.error("JSON serialization failed for data:{}, exception:{} ", data, e);
            // 抛出自定义或具体异常
            throw e;
        }
    }
}
