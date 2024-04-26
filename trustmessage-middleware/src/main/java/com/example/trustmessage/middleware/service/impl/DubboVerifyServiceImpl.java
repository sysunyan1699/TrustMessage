package com.example.trustmessage.middleware.service.impl;

import com.example.trustmessage.middlewareapi.common.MiddlewareMessage;
import com.example.trustmessage.middleware.service.GenericVerifyService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.service.GenericService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
public class DubboVerifyServiceImpl implements GenericVerifyService {

    private static final Logger logger = LoggerFactory.getLogger(DubboVerifyServiceImpl.class);

    private final Cache<String, GenericService> serviceCache;

    @Value("${dubbo.cache.maxSize:100}")
    private int cacheMaxSize;

    @Value("${dubbo.cache.expireAfterAccessMinutes:60}")
    private long cacheExpireAfterAccessMinutes;


    @Value("${spring.application.name:com.example.trustmessage.consumer}")
    private String springApplicationName;


    @Value("${generic.verify.interface:com.example.trustmessage.api.VerifyMessageService}")
    private String genericVerifyInterface;
    @Value("${generic.rpc.method:verifyMessage}")
    private String genericRpcMethod;

    public DubboVerifyServiceImpl() {
        serviceCache = CacheBuilder.newBuilder()
                .maximumSize(100) // 设置最大缓存数
                .expireAfterAccess(60, TimeUnit.MINUTES) // 设置缓存项在给定时间内没有被读/写访问则过期
                .build(); // 注意这里不再提供CacheLoader
    }

    @Override
    public int invoke(int bizID, String messageKey, MiddlewareMessage.VerifyInfo verifyInfo) {
        try {
            String cacheKey = generateCacheKey(verifyInfo);
            GenericService genericService = serviceCache.get(cacheKey, () -> buildGenericService(verifyInfo));
            Object result = genericService.$invoke(genericRpcMethod, new String[]{"java.lang.Integer", "java.lang.String"}, new Object[]{bizID, messageKey});
            return (int) result;
        } catch (Exception e) {
            // 添加日志记录、错误处理或重试逻辑
            logger.error("invoke, bizID:{}, messageKey;{}, verifyInfo:{}, error:{}", bizID, messageKey, verifyInfo, e);
        }

        return -1;
    }


    private GenericService buildGenericService(MiddlewareMessage.VerifyInfo verifyInfo) {

        ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
        reference.setApplication(new ApplicationConfig(springApplicationName));

        RegistryConfig registry = new RegistryConfig();
        registry.setProtocol(verifyInfo.getRegistryProtocol());
        registry.setAddress(verifyInfo.getRegistryAddress());
        reference.setRegistry(registry);

        reference.setInterface(genericVerifyInterface);

        reference.setGeneric(true);
        reference.setUrl(verifyInfo.getUrl());
        reference.setVersion(verifyInfo.getVersion());

        return reference.get();
    }

    // 根据verifyInfo生成一个唯一的缓存键
    private String generateCacheKey(MiddlewareMessage.VerifyInfo verifyInfo) {
        return verifyInfo.getRegistryProtocol() + "-"
                + verifyInfo.getRegistryAddress() + "-"
                + verifyInfo.getUrl() + "-"
                + verifyInfo.getVersion();
    }
}
