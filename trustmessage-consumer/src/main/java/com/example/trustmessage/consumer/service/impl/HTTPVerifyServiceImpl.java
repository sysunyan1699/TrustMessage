package com.example.trustmessage.consumer.service.impl;

import com.example.trustmessage.common.CommonHttpResponse;
import com.example.trustmessage.common.MessageStatus;
import com.example.trustmessage.common.MiddlewareMessage;
import com.example.trustmessage.consumer.service.GenericVerifyService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


//    {
//        "status": "SUCCESS",
//        "message": "Message retrieved successfully.",
//        "data": {
//            "messageKey": "123456",
//            "messageStatus": "commit",// or prepare or rollback
//        }
//    }
@Service
public class HTTPVerifyServiceImpl implements GenericVerifyService {
//    @Autowired
//    private RestTemplate restTemplate;

//    @Autowired
//    public HTTPVerifyServiceImpl(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }

    @Override
    public int invoke(int bizID, String messageKey, MiddlewareMessage.VerifyInfo verifyInfo) {
        // 构造请求的URL
        String url = verifyInfo.getUrl();
        //http://127.0.0.1:8082/verifyMessage?bizID=2&messageKey=Key2
        String realUrl = url + "?bizID=" + bizID + "&messageKey=" + messageKey;
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<CommonHttpResponse> response = restTemplate.getForEntity(realUrl, CommonHttpResponse.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            CommonHttpResponse.Data data = response.getBody().getData();
            return data.getMessageStatus();

        } else {
            // 处理错误响应
        }
        // 返回响应体（根据需要处理和转换响应体）

        return -1;

    }
}
