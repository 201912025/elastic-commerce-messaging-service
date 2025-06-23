package com.example.ElasticCommerce_mail_service.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${email.mock.url}")
    private String emailMockUrl;

    public Mono<Void> sendSimpleEmail(String to, String subject, String text) {
        return Mono.fromRunnable(() -> {
                       SimpleMailMessage msg = new SimpleMailMessage();
                       msg.setFrom("michi2012@naver.com");
                       msg.setTo(to);
                       msg.setSubject(subject);
                       msg.setText(text);
                       mailSender.send(msg);
                       log.info("이메일 전송 완료: {}", to);
                   })
                   // JavaMailSender는 블로킹 I/O이므로 boundedElastic 스케줄러에서 실행
                   .subscribeOn(Schedulers.boundedElastic())
                   .doOnError(e -> log.error("이메일 전송 실패 ({}): {}", to, e.getMessage()))
                   .then();
    }
}
