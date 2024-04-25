package com.example.trustmessage.middlewareclient.controller;

import com.example.trustmessage.middlewareapi.common.HTTPVerifyResponse;
import com.example.trustmessage.middlewareapi.common.MessageStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VerifyController {
    private static final Logger logger = LoggerFactory.getLogger(VerifyController.class);

    @GetMapping("/verifyMessage")
    public ResponseEntity<?> verifyMessage(@RequestParam int bizID, @RequestParam String messageKey) {
        // todo 根据messageKey查询对应业务数据的处理结果
        //MessageStatus status = getMessageStatus(messageId);
        HTTPVerifyResponse.Data data = new HTTPVerifyResponse.Data(messageKey, MessageStatus.PREPARE.getValue());
        HTTPVerifyResponse r = new HTTPVerifyResponse("200", "success", data);


        logger.info("verifyMessage, bizID:{}, messageKey;{}, result;{}", bizID, messageKey, r.toString());

        return ResponseEntity.status(200).body(r);
    }

    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello, World!";
    }
}

