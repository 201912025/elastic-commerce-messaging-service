package com.example.ElasticCommerce_mail_service.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SlackService {

    private final WebClient webClient;

    @Value("${slack.webhook.url}")
    private String webhookUrl;


    /**
     * Slack Incoming Webhook에 메시지를 논블로킹으로 전송
     */
    public Mono<Void> sendMessage(String text) {
        return webClient.post()
                        .uri(webhookUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(Map.of("text", text))
                        .retrieve()
                        .toBodilessEntity()
                        .doOnSuccess(response -> {
                            if (response.getStatusCode().is2xxSuccessful()) {
                                log.info("Slack 메시지 전송 성공");
                            } else {
                                log.error("Slack 전송 실패: HTTP {}", response.getStatusCodeValue());
                            }
                        })
                        .doOnError(e -> log.error("Slack 전송 중 예외 발생: {}", e.getMessage(), e))
                        .then();
    }
}
