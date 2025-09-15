package com.delivery.payment.api.dto.paymentPixRequest;

import java.math.BigDecimal;

import com.delivery.payment.api.dto.payer.PayerDTO;

import lombok.Builder;

@Builder
public record PaymentPixMercadoPagoRequestDTO(BigDecimal transaction_amount, String description, String payment_method_id, PayerDTO payer){    
}
