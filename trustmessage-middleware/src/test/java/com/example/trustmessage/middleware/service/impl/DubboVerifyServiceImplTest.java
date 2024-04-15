package com.example.trustmessage.middleware.service.impl;

import com.example.trustmessage.middlewareapi.common.MiddlewareMessage;
import com.example.trustmessage.middlewareapi.common.VerifyProtocolType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DubboVerifyServiceImplTest {

    @Autowired
    private DubboVerifyServiceImpl dubboVerifyService;

    @Test
    void invoke() {

        MiddlewareMessage.VerifyInfo verifyInfo = new MiddlewareMessage.VerifyInfo(
                VerifyProtocolType.RPC_DUBBO.getValue(),
                "zookeeper",
                "127.0.0.1:2181",
                "dubbo://localhost:12346",
                "1.0.0"
        );

        int result = dubboVerifyService.invoke(1, "key1", verifyInfo);
        assertEquals(1, result);

    }
}