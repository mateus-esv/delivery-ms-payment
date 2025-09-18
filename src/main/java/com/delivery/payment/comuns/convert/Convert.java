package com.delivery.payment.comuns.convert;

import org.springframework.stereotype.Component;

import com.delivery.payment.api.dto.paymentCreditCardRequest.PaymentCreditCardMercadoPagoRequestDTO;
import com.delivery.payment.api.dto.paymentCreditCardRequest.PaymentCreditCardRequestDTO;
import com.delivery.payment.api.dto.paymentCreditCardResponse.PaymentCreditCardToOrchestratorResponseDTO;
import com.delivery.payment.api.dto.paymentPixRequest.PaymentPixMercadoPagoRequestDTO;
import com.delivery.payment.api.dto.paymentPixRequest.PaymentPixRequestDTO;
import com.delivery.payment.api.dto.paymentPixResponse.PaymentPixResponseDTO;
import com.delivery.payment.api.dto.paymentPixResponse.PaymentPixToOrchestratorResponseDTO;
import com.delivery.payment.domain.entity.Payment;

@Component
public class Convert {

    public PaymentCreditCardMercadoPagoRequestDTO convertPaymentCreditCardRequestDTOToPaymentCreditCardMercadoPagoRequestDTO(
            PaymentCreditCardRequestDTO paymentCreditCardRequestDTO) {

        return PaymentCreditCardMercadoPagoRequestDTO
                .builder()
                .transaction_amount(paymentCreditCardRequestDTO.transaction_amount())
                .token(paymentCreditCardRequestDTO.token())
                .description(paymentCreditCardRequestDTO.description())
                .installments(paymentCreditCardRequestDTO.installments())
                .payment_method_id(paymentCreditCardRequestDTO.payment_method_id())
                .payer(paymentCreditCardRequestDTO.payer())
                .build();

    }

    public PaymentPixMercadoPagoRequestDTO convertPaymentPixRequestDTOToPaymentPixMercadoPagoRequestDTO(
            PaymentPixRequestDTO paymentPixRequestDTO) {
        return PaymentPixMercadoPagoRequestDTO.builder()
                .transaction_amount(paymentPixRequestDTO.transaction_amount()) // valor total da transação
                .description(paymentPixRequestDTO.description()) // descrição do pagamento
                .payment_method_id("pix") // PIX é o método
                .payer(paymentPixRequestDTO.payer()) // dados do comprador
                .build();

    }

    public PaymentPixToOrchestratorResponseDTO convertPaymentToPaymentPixToOrchestratorResponseDTO(Payment payment,
            PaymentPixResponseDTO paymentPixResponseDTO) {
        return PaymentPixToOrchestratorResponseDTO.builder()
                .id(payment.getId())
                .customerIDFK(payment.getCustomerIDFK())
                .orderIDFK(payment.getOrderIDFK())
                .paymentDate(payment.getPaymentDate())
                .paymentMethod(payment.getPaymentMethod())
                .idPaymentMercadoPago(payment.getIdPaymentMercadoPago())
                .paymentStatus(payment.getPaymentStatus())
                .statusDetailPaymentMercadoPago(payment.getStatusDetailPaymentMercadoPago())
                .transactionAmount(payment.getTransactionAmount())
                .point_of_interaction(paymentPixResponseDTO.point_of_interaction())
                .build();
    }

    public PaymentCreditCardToOrchestratorResponseDTO convertPaymentToPaymentCreditCardToOrchestratorResponseDTO(
            Payment payment) {
        return PaymentCreditCardToOrchestratorResponseDTO.builder()
                .id(payment.getId())
                .customerIDFK(payment.getCustomerIDFK())
                .orderIDFK(payment.getOrderIDFK())
                .paymentDate(payment.getPaymentDate())
                .paymentMethod(payment.getPaymentMethod())
                .idPaymentMercadoPago(payment.getIdPaymentMercadoPago())
                .paymentStatus(payment.getPaymentStatus())
                .statusDetailPaymentMercadoPago(payment.getStatusDetailPaymentMercadoPago())
                .transactionAmount(payment.getTransactionAmount())
                .build();
    }

}
