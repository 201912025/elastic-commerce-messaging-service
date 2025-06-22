package com.example.ElasticCommerce_mail_service.notification.dto;

import jakarta.validation.constraints.NotNull;

public record NotificationRequest(
        @NotNull Long   targetId,
        @NotNull String eventType,
        String          userEmail,
        @NotNull Long   totalPrice
) {}
