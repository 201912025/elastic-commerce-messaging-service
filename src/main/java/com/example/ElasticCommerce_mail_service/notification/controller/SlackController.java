package com.example.ElasticCommerce_mail_service.notification.controller;

import com.example.ElasticCommerce_mail_service.notification.dto.MessageRequest;
import com.example.ElasticCommerce_mail_service.notification.service.SlackService;
import com.example.ElasticCommerce_mail_service.notification.service.SlackSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/slack")
public class SlackController {

    private final SlackService slackService;
    private final SlackSyncService slackSyncService;

    @PostMapping("/messages")
    public Mono<ResponseEntity<String>> sendSlack(@RequestBody MessageRequest messageRequest) {
        return slackService.sendMessage(messageRequest.text())
                           .thenReturn(ResponseEntity.ok("Slack 메시지 요청 완료"))
                           .onErrorResume(e ->
                                   Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                                           .body("전송 실패: " + e.getMessage()))
                           );
    }

    @PostMapping("/messages/sync")
    public ResponseEntity<String> sendSync(@RequestBody MessageRequest messageRequest) {
        try {
            slackSyncService.sendMessageSync(messageRequest.text());
            return ResponseEntity.ok("Slack 동기 메시지 전송 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("전송 실패: " + e.getMessage());
        }
    }

}
