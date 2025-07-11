# Strikezone 프로젝트 별도 분리 메시지 전송 서비스

Spring Boot 기반 **ElasticCommerce Mail & Slack Notification Service**의 주요 기능 정리한 README 예시입니다.

---

## 📖 개요

`NotificationService`는 주문/결제 이벤트 발생 시 고객에게 **이메일**과 **Slack 메시지**로 알림을 보내는 마이크로서비스입니다.  
- **이벤트 종류**: 주문 완료, 주문 취소, 결제 완료, 그 외 커스텀 이벤트  
- **알림 채널**:  
  - 이메일 (JavaMailSender + Reactor → 비동기/논블로킹)  
  - Slack (WebClient → REST Webhook)

---

## 🚀 주요 기능

1. **이메일 & Slack 동시 발송**  
   - `sendAll(NotificationRequest req)` 호출 한 번으로 두 채널 동시 알림  
   - Reactor `Mono` 워크플로우로 비동기 처리  

2. **이벤트 타입별 커스텀 메시지**  
   - `ORDER_COMPLETED`, `ORDER_CANCELLED`, `PAYMENT_COMPLETED` 등 주요 이벤트에 맞춰  
   - 제목(subject)과 본문(body)을 동적으로 생성  

3. **비동기/논블로킹 I/O**  
   - **이메일**: `JavaMailSender`(블로킹) → `Schedulers.boundedElastic()` 스케줄러에서 실행  
   - **Slack**: `WebClient` 논블로킹 HTTP POST 호출  

4. **성공·실패 로깅**  
   - `.doOnSuccess` / `.doOnError` 콜백으로 채널별 전송 결과 로깅  
   - 문제 시 예외 메시지와 스택트레이스 출력

---

## ⚙️ 기술 스택

|분류|기술 / 라이브러리|
|---|-----------------|
|언어·플랫폼|Java 17, Spring Boot
|리액티브|Sping WebFlux|
|이메일|Spring JavaMailSender|
