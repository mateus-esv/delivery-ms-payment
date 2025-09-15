package com.delivery.payment.domain.service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delivery.payment.api.dto.paymentCreditCardRequest.PaymentCreditCardRequestDTO;
import com.delivery.payment.api.dto.paymentCreditCardResponse.PaymentCreditCardResponseDTO;
import com.delivery.payment.api.dto.paymentCreditCardRequest.PaymentCreditCardMercadoPagoRequestDTO;
import com.delivery.payment.api.dto.paymentPixRequest.PaymentPixMercadoPagoRequestDTO;
import com.delivery.payment.api.dto.paymentPixRequest.PaymentPixRequestDTO;
import com.delivery.payment.api.dto.paymentPixResponse.PaymentPixResponseDTO;
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

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private Dotenv dotenv = Dotenv.load();

    private String token = dotenv.get("TOKEN_MERCADO_PAGO_TESTE");

    public void createCreditCardPayment(PaymentCreditCardRequestDTO paymentCreditCardRequestDTO) {

        // convertemos o dto, pois o dto não pode ter o id do cliente nele
        PaymentCreditCardMercadoPagoRequestDTO paymentCreditCardMercadoPagoRequestDTO = PaymentCreditCardMercadoPagoRequestDTO
                .builder()
                .transaction_amount(paymentCreditCardRequestDTO.transaction_amount())
                .token(paymentCreditCardRequestDTO.token())
                .description(paymentCreditCardRequestDTO.description())
                .installments(paymentCreditCardRequestDTO.installments())
                .payment_method_id(paymentCreditCardRequestDTO.payment_method_id())
                .payer(paymentCreditCardRequestDTO.payer())
                .build();

        this.token = "Bearer " + this.token;

        // enviamos os dados para o mercado pago para realizar o pagamento
        PaymentCreditCardResponseDTO paymentResponseDTO = request.createCreditCardPayment(this.token,
                UUID.randomUUID().toString(),
                paymentCreditCardMercadoPagoRequestDTO);

        // salvamos no banco
        // pegamos alguns dados da requisição de pagamento para salvar
        UUID id = UUID.randomUUID(); // id do pagamento
        UUID idOrder = UUID.randomUUID(); // geramos um id para pedido

        Payment payment = Payment.builder()
                .id(id)
                .customerIDFK(paymentCreditCardRequestDTO.idCustomer())
                .orderIDFK(idOrder)
                .paymentDate(LocalDateTime.now())
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .idPaymentMercadoPago(paymentResponseDTO.id())
                .statusPaymentMercadoPago(paymentResponseDTO.status())
                .statusDetailPaymentMercadoPago(paymentResponseDTO.status_detail())
                .transactionAmount(paymentCreditCardRequestDTO.transaction_amount())
                .build();

        repository.save(payment);

        checkPaymentCreditCard(String.valueOf(paymentResponseDTO.id()));
    }

    private void checkPaymentCreditCard(String paymentID) {
        PaymentCreditCardResponseDTO paymentCreditCardResponseDTO = request.getPaymentCreditCard(this.token, paymentID);

        // approved, pending, rejected, cancelled
        if (paymentCreditCardResponseDTO.status().equals("approved")) {
            // enviar pedido para o microsserviço de pedido
            // chamar endpoint de cliente para atualizar sobre a pontuação
            System.out.println(
                    "entrou para fazer requisição no microsserviço de pedido, pois foi realizado o pagamento com sucesso!");
        }

    }

    public PaymentPixResponseDTO createPIXPayment(PaymentPixRequestDTO paymentPixRequestDTO) {
        // DTO para pagamento PIX
        PaymentPixMercadoPagoRequestDTO paymentPixMercadoPagoRequestDTO = PaymentPixMercadoPagoRequestDTO.builder()
                .transaction_amount(paymentPixRequestDTO.transaction_amount()) // valor total da transação
                .description(paymentPixRequestDTO.description()) // descrição do pagamento
                .payment_method_id("pix") // PIX é o método
                .payer(paymentPixRequestDTO.payer()) // dados do comprador
                .build();

        this.token = "Bearer " + this.token;

        // enviar requisição para o Mercado Pago
        PaymentPixResponseDTO paymentPixResponseDTO = request.createPIXPayment(
                this.token,
                UUID.randomUUID().toString(), // id único da transação
                paymentPixMercadoPagoRequestDTO);

        // salvar no banco
        UUID idPayment = UUID.randomUUID();
        UUID idOrder = UUID.randomUUID();

        Payment payment = Payment.builder()
                .id(idPayment)
                .customerIDFK(paymentPixRequestDTO.idCustomer())
                .orderIDFK(idOrder)
                .paymentDate(LocalDateTime.now())
                .paymentMethod(PaymentMethod.PIX)
                .idPaymentMercadoPago(paymentPixResponseDTO.id())
                .statusPaymentMercadoPago(paymentPixResponseDTO.status())
                .statusDetailPaymentMercadoPago(paymentPixResponseDTO.status_detail())
                .transactionAmount(paymentPixRequestDTO.transaction_amount())
                .build();

        repository.save(payment);
            
        // monitorar pagamento
        checkPaymentPixInBackground(String.valueOf(paymentPixResponseDTO.id()));
            
        // Retornar QR Code para front-end gerar o pagamento
        return paymentPixResponseDTO;
    }

    public void checkPaymentPixInBackground(String paymentID) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    PaymentPixResponseDTO paymentPixResponseDTO = request.getPaymentPix(token, paymentID);

                    if ("approved".equals(paymentPixResponseDTO.status())) {
                        // pagamento aprovado
                        System.out.println("Pagamento PIX aprovado! Liberando pedido...");

                        // TODO: chamar microsserviço de pedido ou atualizar cliente

                        


                        // cancelar futuras execuções do loop
                        throw new InterruptedException("Pagamento aprovado, finalizando loop.");
                    } else {
                        System.out.println("Pagamento ainda pendente. Tentando novamente em 5 segundos...");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        // agenda a execução a cada 5 segundos
        scheduler.scheduleAtFixedRate(task, 0, 5, TimeUnit.SECONDS);
    }

}
