package com.example.trustmessage.middleware.service.impl;

import com.example.trustmessage.middleware.utils.MessageUtils;
import com.example.trustmessage.middlewareapi.common.HTTPVerifyResponse;
import com.example.trustmessage.middlewareapi.common.MiddlewareMessage;
import com.example.trustmessage.middleware.service.GenericVerifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * HTTP 接口返回值预定义格式
 * {
 * "status": "SUCCESS",
 * "message": "Message retrieved successfully.",
 * "data": {
 * "messageKey": "123456",
 * "messageStatus": "commit",// or prepare or rollback
 * }
 * }
 **/
@Service
public class HTTPVerifyServiceImpl implements GenericVerifyService {

    private static final Logger logger = LoggerFactory.getLogger(HTTPVerifyServiceImpl.class);

    @Override
    public int invoke(int bizID, String messageKey, MiddlewareMessage.VerifyInfo verifyInfo) {
        // 构造请求的URL
        String url = MessageUtils.getHttpVerifyURL(bizID, messageKey, verifyInfo.getUrl());
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<HTTPVerifyResponse> response = restTemplate.getForEntity(url, HTTPVerifyResponse.class);

        logger.info("invoke, url:{}, response;{}", url, response);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            HTTPVerifyResponse.Data data = response.getBody().getData();
            return data.getMessageStatus();

        }
        return -1;

    }
}
