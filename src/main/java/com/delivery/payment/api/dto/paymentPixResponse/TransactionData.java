package com.delivery.payment.api.dto.paymentPixResponse;

import lombok.Builder;

@Builder
public record TransactionData(
        String qr_code,
        String qr_code_base64) {
}