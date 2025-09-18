package com.delivery.payment.api.dto.paymentCreditCardResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.delivery.payment.comuns.enums.PaymentMethod;
import com.delivery.payment.comuns.enums.PaymentStatus;

import lombok.Builder;

@Builder
public record PaymentCreditCardToOrchestratorResponseDTO(
                UUID id,
                UUID orderIDFK, 
                UUID customerIDFK, 
                LocalDateTime paymentDate, 
                PaymentMethod paymentMethod, 
                Long idPaymentMercadoPago, 
                PaymentStatus paymentStatus, 
                String statusDetailPaymentMercadoPago, 
                BigDecimal transactionAmount 
) {
}