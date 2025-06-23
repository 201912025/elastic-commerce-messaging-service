package com.example.ElasticCommerce_mail_service.notification.service;

import com.example.ElasticCommerce_mail_service.notification.dto.NotificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final EmailService emailService;
    private final SlackService slackService;

    @Value("${shop.frontend.url}")
    private String shopFrontendUrl;

    public void sendAll(NotificationRequest req) {
        String orderLink = shopFrontendUrl + "/orders/" + req.targetId();

        String subject, body;
        switch (req.eventType()) {
            case "ORDER_COMPLETED" -> {
                subject = "[예시스토어] 주문이 완료되었습니다";
                body = String.format(
                        "안녕하세요, 고객님!\n\n" +
                                "주문번호 %d번이 정상적으로 처리되었습니다.\n" +
                                "총 결제 금액: %,d원\n" +
                                "주문 상세 보기: %s\n\n" +
                                "감사합니다. 예시스토어 드림",
                        req.targetId(), req.totalPrice(), orderLink
                );
            }
            case "ORDER_CANCELLED" -> {
                subject = "[예시스토어] 주문이 취소되었습니다";
                body = String.format(
                        "안녕하세요, 고객님!\n\n" +
                                "주문번호 %d번이 취소 처리되었습니다.\n" +
                                "환불 금액: %,d원\n" +
                                "주문 상세 보기: %s\n\n" +
                                "감사합니다.",
                        req.targetId(), req.totalPrice(), orderLink
                );
            }
            case "PAYMENT_COMPLETED" -> {
                subject = "[예시스토어] 결제가 완료되었습니다";
                body = String.format(
                        "안녕하세요, 고객님!\n\n" +
                                "결제 ID %d번이 성공적으로 처리되었습니다.\n" +
                                "결제 금액: %,d원\n" +
                                "주문 상세 보기: %s\n\n" +
                                "감사합니다. 예시스토어 드림",
                        req.targetId(), req.totalPrice(), orderLink
                );
            }
            default -> {
                subject = "[예시스토어] 알림이 도착했습니다";
                body = String.format(
                        "알림: %s (ID: %d)\n보기: %s",
                        req.eventType(), req.targetId(), orderLink
                );
            }
        }

        if (req.userEmail() != null && !req.userEmail().isBlank()) {
            emailService.sendSimpleEmail(req.userEmail(), subject, body)
                        .doOnSuccess(u -> log.info("Email 전송 성공: {}", req.userEmail()))
                        .doOnError(e -> log.error("Email 전송 실패: {}", req.userEmail(), e))
                        .subscribe();
        }

        String slackMessage = subject + "\n\n" + body;
        slackService.sendMessage(slackMessage)
                    .doOnSuccess(unused -> log.info("Slack 알림 전송 성공"))
                    .doOnError(error -> log.error("Slack 알림 전송 실패", error))
                    .subscribe();

    }
}
