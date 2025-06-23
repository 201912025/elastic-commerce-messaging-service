package com.example.ElasticCommerce_mail_service.notification.service;

import com.example.ElasticCommerce_mail_service.notification.dto.NotificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final EmailService emailService;
    private final SlackService slackService;

    @KafkaListener(topics = "notification-topic", groupId = "notification-group")
    public void handleNotification(NotificationRequest req) {
        String subject = buildSubject(req);
        String body = buildBody(req);
        String fullMessage = subject + "\n\n" + body;

        if (req.userEmail() != null && !req.userEmail().isBlank()) {
            emailService.sendSimpleEmail(req.userEmail(), subject, body)
                        .doOnSuccess(v -> log.info("이메일 전송 성공: {}", req.userEmail()))
                        .doOnError(e -> log.error("이메일 전송 실패: {}", req.userEmail(), e))
                        .subscribe();
        }

        slackService.sendMessage(fullMessage)
                    .doOnSuccess(v -> log.info("Slack 메시지 전송 성공"))
                    .doOnError(e -> log.error("Slack 메시지 전송 실패", e))
                    .subscribe();
    }

    private String buildSubject(NotificationRequest req) {
        return switch (req.eventType()) {
            case "ORDER_COMPLETED"   -> "[예시스토어] 주문이 완료되었습니다";
            case "ORDER_CANCELLED"   -> "[예시스토어] 주문이 취소되었습니다";
            case "PAYMENT_COMPLETED" -> "[예시스토어] 결제가 완료되었습니다";
            default                   -> "[예시스토어] 알림이 도착했습니다";
        };
    }

    private String buildBody(NotificationRequest req) {
        String orderLink = "https://your-shop-frontend.com/orders/" + req.targetId();
        return switch (req.eventType()) {
            case "ORDER_COMPLETED" ->
                    String.format(
                            "안녕하세요, 고객님!\n\n" +
                                    "주문번호 %d번이 정상적으로 처리되었습니다.\n" +
                                    "총 결제 금액: %,d원\n" +
                                    "주문 상세 보기: %s\n\n" +
                                    "감사합니다. 예시스토어 드림",
                            req.targetId(), req.totalPrice(), orderLink
                    );
            case "ORDER_CANCELLED" ->
                    String.format(
                            "안녕하세요, 고객님!\n\n" +
                                    "주문번호 %d번이 취소 처리되었습니다.\n" +
                                    "환불 금액: %,d원\n" +
                                    "주문 상세 보기: %s\n\n" +
                                    "감사합니다.",
                            req.targetId(), req.totalPrice(), orderLink
                    );
            case "PAYMENT_COMPLETED" ->
                    String.format(
                            "안녕하세요, 고객님!\n\n" +
                                    "결제 ID %d번이 성공적으로 처리되었습니다.\n" +
                                    "결제 금액: %,d원\n" +
                                    "주문 상세 보기: %s\n\n" +
                                    "감사합니다. 예시스토어 드림",
                            req.targetId(), req.totalPrice(), orderLink
                    );
            default ->
                    String.format(
                            "알림: %s (ID: %d)\n주문 상세 보기: %s",
                            req.eventType(), req.targetId(), orderLink
                    );
        };
    }
}
