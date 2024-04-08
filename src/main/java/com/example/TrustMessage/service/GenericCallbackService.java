package com.example.trustmessage.consumer.service;

public interface GenericCallbackService {
    Object invoke(String service, String method, Object... params);
}
