package com.example.trustmessage.middleware.service;

import com.example.trustmessage.middlewareapi.common.VerifyProtocolType;
import com.example.trustmessage.middleware.service.impl.DubboVerifyServiceImpl;
import com.example.trustmessage.middleware.service.impl.HTTPVerifyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class VerifyServiceFactory {
    @Autowired
    private HTTPVerifyServiceImpl httpVerifyService;

    @Autowired
    private DubboVerifyServiceImpl dubboVerifyService;

    public GenericVerifyService getVerifyService(int protocolType) {
        switch (VerifyProtocolType.valueOf(protocolType)) {
            case HTTP:
                return httpVerifyService;
            case RPC_DUBBO:
                return dubboVerifyService;
            default:
                throw new IllegalArgumentException("Unsupported protocol: " + protocolType);
        }
    }
}
