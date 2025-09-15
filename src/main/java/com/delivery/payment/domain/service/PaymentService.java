package com.delivery.payment.domain.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delivery.payment.api.dto.PaymentResponseDTO;
import com.delivery.payment.api.dto.paymentRequest.PaymentCreditCardMSRequestDTO;
import com.delivery.payment.api.dto.paymentRequest.PaymentCreditCardMercadoPagoRequestDTO;
import com.delivery.payment.comuns.enums.PaymentMethod;
import com.delivery.payment.domain.entity.Payment;
import com.delivery.payment.domain.repository.PaymentRepository;
import com.delivery.payment.domain.request.PaymentRequest;

import io.github.cdimascio.dotenv.Dotenv;

@Service
public class PaymentService {

    @Autowired
    private PaymentRequest request;

    @Autowired
    private PaymentRepository repository;

    private Dotenv dotenv = Dotenv.load();

    private String token = dotenv.get("TOKEN_MERCADO_PAGO_TESTE");

    public void createCreditCardPayment(PaymentCreditCardMSRequestDTO paymentCreditCardMSRequestDTO) {

        // convertemos o dto, pois o dto não pode ter o id do cliente nele
        PaymentCreditCardMercadoPagoRequestDTO paymentCreditCardMercadoPagoRequestDTO = PaymentCreditCardMercadoPagoRequestDTO
                .builder()
                .transaction_amount(paymentCreditCardMSRequestDTO.transaction_amount())
                .token(paymentCreditCardMSRequestDTO.token())
                .description(paymentCreditCardMSRequestDTO.description())
                .installments(paymentCreditCardMSRequestDTO.installments())
                .payment_method_id(paymentCreditCardMSRequestDTO.payment_method_id())
                .payer(paymentCreditCardMSRequestDTO.payer())
                .build();

        this.token = "Bearer " + this.token;

        // enviamos os dados para o mercado pago para realizar o pagamento
        PaymentResponseDTO paymentResponseDTO = request.createCreditCardPayment(this.token,
                UUID.randomUUID().toString(),
                paymentCreditCardMercadoPagoRequestDTO);

        // salvamos no banco
        // pegamos alguns dados da requisição de pagamento para salvar
        UUID id = UUID.randomUUID(); // id do pagamento
        UUID idOrder = UUID.randomUUID(); // geramos um id para pedido

        Payment payment = Payment.builder()
                .id(id)
                .customerIDFK(paymentCreditCardMSRequestDTO.idCustomer())
                .orderIDFK(idOrder)
                .paymentDate(LocalDateTime.now())
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .idPaymentMercadoPago(paymentResponseDTO.id())
                .statusPaymentMercadoPago(paymentResponseDTO.status())
                .statusDetailPaymentMercadoPago(paymentResponseDTO.status_detail())
                .transactionAmount(paymentCreditCardMSRequestDTO.transaction_amount())
                .build();

        repository.save(payment);

        checkPayment(String.valueOf(paymentResponseDTO.id()));
    }

    public void createPIXPayment() {

    }

    private void checkPayment(String paymentID) {
        PaymentResponseDTO paymentResponseDTO = request.getPayment(this.token, paymentID);

        // approved, pending, rejected, cancelled
        if (paymentResponseDTO.status().equals("approved")) {
            // enviar pedido para o microsserviço de pedido
            // chamar endpoint de cliente para atualizar sobre a pontuação
            System.out.println(
                    "entrou para fazer requisição no microsserviço de pedido, pois foi realizado o pagamento com sucesso!");
        }

    }

}
