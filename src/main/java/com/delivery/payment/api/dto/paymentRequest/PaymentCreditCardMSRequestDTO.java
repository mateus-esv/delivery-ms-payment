package com.delivery.payment.api.dto.paymentRequest;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Builder;

@Builder
public record PaymentCreditCardMSRequestDTO(
        UUID idCustomer, // id do cliente que está fazendo o pagamento
        BigDecimal transaction_amount, // valor da transação
        String token, // card token gerado no front
        String description, // descroção da compra
        Integer installments, // quantidade de parcelas
        String payment_method_id, // meio de pagamento: pix, visa, master...
        PayerDTO payer // dados do pagador
        ) {}