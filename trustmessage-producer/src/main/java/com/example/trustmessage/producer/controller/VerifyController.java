package com.example.trustmessage.producer.controller;

import com.example.trustmessage.common.CommonHttpResponse;
import com.example.trustmessage.common.MessageStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VerifyController {
    @GetMapping("/verifyMessage")
    public ResponseEntity<?> verifyMessage(@RequestParam int bizID, @RequestParam String messageKey) {
        System.out.println("HTTP verifyMessage: " + "bizID=" + bizID + "messageKey=" + messageKey);
        // 示例逻辑：根据messageKey查询对应业务数据的处理结果
        //MessageStatus status = getMessageStatus(messageId);
        CommonHttpResponse.Data data = new CommonHttpResponse.Data(messageKey, MessageStatus.PREPARE.getValue());
        CommonHttpResponse r = new CommonHttpResponse("200", "success", data);

        return ResponseEntity.status(200).body(r);
    }

    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello, World!";
    }
}

