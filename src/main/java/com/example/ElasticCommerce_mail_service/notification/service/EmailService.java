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
public class EmailService {

    private final RestTemplate restTemplate;

    // application.yml 에 설정한 테스트용 mock 이메일 URL
    @Value("${email.mock.url}")
    private String emailMockUrl;

    /**
     * mock 이메일 API를 동기(블로킹) POST 호출로 500ms 지연을 시뮬레이션합니다.
     */
    public void sendSimpleEmail(String to, String subject, String text) {
        Map<String, String> payload = Map.of(
                "to",      to,
                "subject", subject,
                "text",    text
        );

        try {
            ResponseEntity<Void> resp = restTemplate.postForEntity(emailMockUrl, payload, Void.class);
            if (resp.getStatusCode().is2xxSuccessful()) {
                log.info("Mock email sent to {} [status={}]", to, resp.getStatusCodeValue());
            } else {
                log.error("Mock email failed to {} [status={}]", to, resp.getStatusCodeValue());
            }
        } catch (Exception e) {
            log.error("Mock email send exception to {}: {}", to, e.getMessage(), e);
        }
    }
}
