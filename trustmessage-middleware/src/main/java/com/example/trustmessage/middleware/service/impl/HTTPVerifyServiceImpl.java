package com.example.trustmessage.middleware.service.impl;

import com.example.trustmessage.middlewareapi.common.HTTPVerifyResponse;
import com.example.trustmessage.middlewareapi.common.MiddlewareMessage;
import com.example.trustmessage.middleware.service.GenericVerifyService;
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

    @Override
    public int invoke(int bizID, String messageKey, MiddlewareMessage.VerifyInfo verifyInfo) {
        // 构造请求的URL
        String url = verifyInfo.getUrl();
        String realUrl = url + "?bizID=" + bizID + "&messageKey=" + messageKey;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<HTTPVerifyResponse> response = restTemplate.getForEntity(realUrl, HTTPVerifyResponse.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            HTTPVerifyResponse.Data data = response.getBody().getData();
            return data.getMessageStatus();

        } else {
            // 处理错误响应
        }
        // 返回响应体（根据需要处理和转换响应体）

        return -1;

    }
}
