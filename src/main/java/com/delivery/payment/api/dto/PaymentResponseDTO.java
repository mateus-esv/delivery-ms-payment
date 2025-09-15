package com.delivery.payment.api.dto;

import lombok.Builder;

@Builder
public record PaymentResponseDTO(
        Long id,
        String status,
        String status_detail) {
}