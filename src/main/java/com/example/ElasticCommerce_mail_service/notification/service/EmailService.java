package com.example.ElasticCommerce_mail_service.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final WebClient webClient;

    @Value("${email.mock.url}")
    private String emailMockUrl;


    public Mono<Void> sendSimpleEmail(String to, String subject, String text) {
        Map<String, String> payload = Map.of(
                "to",      to,
                "subject", subject,
                "text",    text
        );

        return webClient.post()
                        .uri(emailMockUrl)
                        .bodyValue(payload)
                        .retrieve()
                        .toBodilessEntity()
                        .doOnSuccess(resp ->
                                log.info("Mock email sent to {} [status={}]", to, resp.getStatusCodeValue())
                        )
                        .doOnError(e ->
                                log.error("Mock email send failed to {}: {}", to, e.getMessage(), e)
                        )
                        .then();
    }
}
