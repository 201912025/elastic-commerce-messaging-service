package com.example.ElasticCommerce_mail_service.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SlackSyncService {

    private final RestTemplate restTemplate;

    @Value("${slack.webhook.url}")
    private String webhookUrl;

    public void sendMessageSync(String text) {
        Map<String, String> payload = Map.of("text", text);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(webhookUrl, payload, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Slack 메시지 전송 성공");
            } else {
                log.error("Slack 전송 실패: HTTP {}", response.getStatusCodeValue());
            }
        } catch (Exception e) {
            log.error("Slack 전송 중 예외 발생: {}", e.getMessage(), e);
            throw e;
        }
    }
}
