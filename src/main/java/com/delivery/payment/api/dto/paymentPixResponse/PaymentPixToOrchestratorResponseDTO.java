package com.delivery.payment.api.dto.paymentPixResponse;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.delivery.payment.comuns.enums.PaymentMethod;
import com.delivery.payment.comuns.enums.PaymentStatus;

@Builder
public record PaymentPixToOrchestratorResponseDTO(
                UUID id,
                UUID orderIDFK,
                UUID customerIDFK,
                LocalDateTime paymentDate,
                PaymentMethod paymentMethod, 
                Long idPaymentMercadoPago, 
                PaymentStatus paymentStatus, 
                String statusDetailPaymentMercadoPago, 
                BigDecimal transactionAmount, 
                PointOfInteraction point_of_interaction) {
}
