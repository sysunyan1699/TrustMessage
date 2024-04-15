package com.example.trustmessage.middleware.service.impl;

import com.example.trustmessage.middlewareapi.common.MiddlewareMessage;
import com.example.trustmessage.middlewareapi.common.VerifyProtocolType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class HTTPVerifyServiceImplTest {


    @Autowired
    private HTTPVerifyServiceImpl httpVerifyService;

    @Test
    void invoke() {

        MiddlewareMessage.VerifyInfo verifyInfo = new MiddlewareMessage.VerifyInfo();
        verifyInfo.setUrl("http://127.0.0.1:8082/verifyMessage");
        verifyInfo.setProtocolType(VerifyProtocolType.HTTP.getValue());
        assertEquals(1, httpVerifyService.invoke(2, "key2", verifyInfo));
    }
}