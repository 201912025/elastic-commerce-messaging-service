package com.example.ElasticCommerce_mail_service.notification.controller;

import com.example.ElasticCommerce_mail_service.notification.dto.NotificationRequest;
import com.example.ElasticCommerce_mail_service.notification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/send")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<Void> send(@Valid @RequestBody NotificationRequest req) {
        notificationService.sendAll(req);
        return ResponseEntity.ok().build();
    }
}
