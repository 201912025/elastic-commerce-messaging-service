package com.example.ElasticCommerce_mail_service.notification.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mock")
public class MockDelayController {

    // 메일 전용 지연 500ms
    @PostMapping("/email")
    public ResponseEntity<Void> mockEmail() throws InterruptedException {
        Thread.sleep(500);
        return ResponseEntity.ok().build();
    }

    // 슬랙 전용 지연 400ms
    @PostMapping("/slack")
    public ResponseEntity<Void> mockSlack() throws InterruptedException {
        Thread.sleep(400);
        return ResponseEntity.ok().build();
    }

    // 통합 지연 700ms
    @PostMapping("/sendAll")
    public ResponseEntity<Void> mockSendAll() throws InterruptedException {
        Thread.sleep(700);
        return ResponseEntity.ok().build();
    }
}
