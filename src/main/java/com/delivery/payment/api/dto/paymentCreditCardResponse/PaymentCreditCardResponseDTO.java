package com.delivery.payment.api.dto.paymentCreditCardResponse;

import lombok.Builder;

@Builder
public record PaymentCreditCardResponseDTO(
        Long id,
        String status,
        String status_detail) {
}