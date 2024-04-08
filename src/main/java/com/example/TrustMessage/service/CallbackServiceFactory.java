package com.example.trustmessage.consumer.service;

public class CallbackServiceFactory {

    public static GenericCallbackService getCallbackService(String protocol) {
        if ("thrift".equalsIgnoreCase(protocol)) {
            return new ThriftCallbackServiceAdapter();
        } else if ("dubbo".equalsIgnoreCase(protocol)) {
            return new DubboCallbackServiceAdapter();
        } else {
            throw new IllegalArgumentException("Unsupported protocol: " + protocol);
        }
    }
}
