package com.example.trustmessage.consumer.service;

import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.*;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;

public class ThriftCallbackServiceAdapter implements GenericCallbackService {

    // 实现泛化调用逻辑
//    @Override
//    public Object invoke(String service, String method, Object... params) {
//        // ... 实现Thrift泛化调用
//        return null; // 替换为实际的Thrift调用逻辑
//    }

    @Value("${thrift.host}")
    private String thriftHost;

    @Value("${thrift.port}")
    private int thriftPort;

    @Override
    public Object invoke(String service, String method, Object... params) {
        TTransport transport = null;
        try {
            // 创建一个TTransport对象来建立到服务端的连接。
            transport = new TSocket(thriftHost, thriftPort);
            transport.open();

            //TProtocol对象用于序列化和反序列化数据。
            TProtocol protocol = new TBinaryProtocol(transport);

            // 构造请求
            protocol.writeMessageBegin(new TMessage("methodName", TMessageType.CALL, 0));
            // 此处根据实际参数类型手动构造参数
            protocol.writeStructBegin(new TStruct("args"));
            protocol.writeFieldBegin(new TField("paramName", TType.STRING, (short) 1));
            protocol.writeString("paramValue");
            protocol.writeFieldEnd();
            protocol.writeFieldStop();
            protocol.writeStructEnd();
            protocol.writeMessageEnd();

            // 发送请求
            protocol.getTransport().flush();
            TServiceClient client = createThriftClient(service, protocol);

            // 反射调用方法
            return client.getClass().getMethod(method, toClasses(params)).invoke(client, params);
        } catch (TTransportException e) {
            // Transport exception handling
            throw new RuntimeException("Thrift transport exception", e);
        } catch (Exception e) {
            // Other exceptions (reflection exceptions, etc.)
            throw new RuntimeException("Error invoking thrift method", e);
        } finally {
            if (transport != null) {
                transport.close();
            }
        }
    }

    private TServiceClient createThriftClient(String service, TProtocol protocol) {
        // 假设有一个方式能根据服务名创建对应的Thrift客户端
        // 这个部分具体实现将依赖于你的Thrift服务定义
        return MyThriftServiceClientFactory.getClient(service, protocol);
    }

    private Class<?>[] toClasses(Object... params) {
        // 转换参数为类类型数组，以用于反射
        return Arrays.stream(params)
                .map(Object::getClass)
                .toArray(Class<?>[]::new);
    }

}
