package com.delivery.payment.api.dto.paymentPixRequest;

import java.math.BigDecimal;
import java.util.UUID;

import com.delivery.payment.api.dto.payer.PayerDTO;

import lombok.Builder;

@Builder
public record PaymentPixRequestDTO(UUID idCustomer, BigDecimal transaction_amount, String description, String payment_method_id, PayerDTO payer){    
}
