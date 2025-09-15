package com.delivery.payment.api.dto.paymentPixResponse;

import lombok.Builder;

@Builder
public record PointOfInteraction(
        TransactionData transaction_data) {
}