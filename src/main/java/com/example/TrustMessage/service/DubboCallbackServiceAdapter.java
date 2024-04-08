package com.example.trustmessage.consumer.service;

public class DubboCallbackServiceAdapter implements GenericCallbackService{
    // 实现泛化调用逻辑
    @Override
    public Object invoke(String service, String method, Object... params) {
        // ... 实现Dubbo泛化调用
        return null; // 替换为实际的Dubbo调用逻辑
    }
}
