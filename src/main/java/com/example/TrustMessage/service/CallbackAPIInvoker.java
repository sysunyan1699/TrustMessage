package com.example.trustmessage.consumer.service;

public class CallbackAPIInvoker{

    private GenericCallbackService callbackService;

    public CallbackAPIInvoker(String protocol) {
        // 获取对应协议的泛化回调服务
        this.callbackService = CallbackServiceFactory.getCallbackService(protocol);
    }

    public int checkMessageStatus(String messageKey) {
        // 调用泛化服务
        Object result = callbackService.invoke("CallbackAPI", "checkMessageStatus", messageKey);

        // 这里假设返回值是Integer类型。在实际应用中，可能需要转换或处理结果
        return (Integer) result;
    }

}
