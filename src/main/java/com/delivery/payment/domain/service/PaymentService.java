package com.delivery.payment.domain.service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delivery.payment.api.dto.paymentCreditCardRequest.PaymentCreditCardRequestDTO;
import com.delivery.payment.api.dto.paymentCreditCardResponse.PaymentCreditCardResponseDTO;
import com.delivery.payment.api.dto.paymentCreditCardResponse.PaymentCreditCardToOrchestratorResponseDTO;
import com.delivery.payment.api.dto.paymentCreditCardRequest.PaymentCreditCardMercadoPagoRequestDTO;
import com.delivery.payment.api.dto.paymentPixRequest.PaymentPixMercadoPagoRequestDTO;
import com.delivery.payment.api.dto.paymentPixRequest.PaymentPixRequestDTO;
import com.delivery.payment.api.dto.paymentPixResponse.PaymentPixResponseDTO;
import com.delivery.payment.api.dto.paymentPixResponse.PaymentPixToOrchestratorResponseDTO;
import com.delivery.payment.comuns.convert.Convert;
import com.delivery.payment.comuns.enums.PaymentMethod;
import com.delivery.payment.comuns.enums.PaymentStatus;
import com.delivery.payment.domain.entity.Payment;
import com.delivery.payment.domain.repository.PaymentRepository;
import com.delivery.payment.domain.request.OrchestratorPaymentRequest;
import com.delivery.payment.domain.request.PaymentRequest;

import io.github.cdimascio.dotenv.Dotenv;

@Service
public class PaymentService {

    @Autowired
    private PaymentRequest paymentRequest;

    @Autowired
    private OrchestratorPaymentRequest orchestratorPaymentRequest;

    @Autowired
    private PaymentRepository repository;

    @Autowired
    private Convert convert;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private Dotenv dotenv = Dotenv.load();

    private String token = dotenv.get("TOKEN_MERCADO_PAGO_PRODUCAO");

    public PaymentCreditCardToOrchestratorResponseDTO createCreditCardPayment(
            PaymentCreditCardRequestDTO paymentCreditCardRequestDTO) {

        // convertemos o dto, pois o dto não pode ter o id do cliente nele
        PaymentCreditCardMercadoPagoRequestDTO paymentCreditCardMercadoPagoRequestDTO = convert.convertPaymentCreditCardRequestDTOToPaymentCreditCardMercadoPagoRequestDTO(paymentCreditCardRequestDTO);

        this.token = "Bearer " + this.token;

        // enviamos os dados para o mercado pago para realizar o pagamento
        PaymentCreditCardResponseDTO paymentCreditCardResponseDTO = paymentRequest.createCreditCardPayment(this.token,
                UUID.randomUUID().toString(),
                paymentCreditCardMercadoPagoRequestDTO);

        // salvamos no banco
        // pegamos alguns dados da requisição de pagamento para salvar
        UUID id = UUID.randomUUID(); // id do pagamento
        UUID idOrder = UUID.randomUUID(); // geramos um id para pedido, esse id deve ser enviado para o microsserviço de
                                          // pedido, pois ele é a pk do id que criamos aqui mesmo ao inves de criar no
                                          // ms de pedido

        Payment payment = Payment.builder()
                .id(id)
                .customerIDFK(paymentCreditCardRequestDTO.idCustomer())
                .orderIDFK(idOrder)
                .paymentDate(LocalDateTime.now())
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .idPaymentMercadoPago(paymentCreditCardResponseDTO.id())
                .paymentStatus(PaymentStatus.valueOf(paymentCreditCardResponseDTO.status().toUpperCase()))
                .statusDetailPaymentMercadoPago(paymentCreditCardResponseDTO.status_detail())
                .transactionAmount(paymentCreditCardRequestDTO.transaction_amount())
                .build();

        repository.save(payment);

        
        return convert.convertPaymentToPaymentCreditCardToOrchestratorResponseDTO(payment);

        // checkPaymentCreditCard(String.valueOf(paymentResponseDTO.id()));
    }

    public PaymentCreditCardResponseDTO checkPaymentCreditCard(Long paymentID) {
        return paymentRequest.getPaymentCreditCard(this.token, paymentID);
    }

    public PaymentPixToOrchestratorResponseDTO createPIXPayment(PaymentPixRequestDTO paymentPixRequestDTO) {
        // DTO para pagamento PIX
        PaymentPixMercadoPagoRequestDTO paymentPixMercadoPagoRequestDTO = convert.convertPaymentPixRequestDTOToPaymentPixMercadoPagoRequestDTO(paymentPixRequestDTO);

        this.token = "Bearer " + this.token;

        // enviar requisição para o Mercado Pago
        PaymentPixResponseDTO paymentPixResponseDTO = paymentRequest.createPIXPayment(
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
                .paymentStatus(PaymentStatus.valueOf(paymentPixResponseDTO.status().toUpperCase()))
                .statusDetailPaymentMercadoPago(paymentPixResponseDTO.status_detail())
                .transactionAmount(paymentPixRequestDTO.transaction_amount())
                .build();

        repository.save(payment);

        // deixa a thread monitorarndo o pagamento
        checkPaymentPixInBackground(paymentPixResponseDTO.id());

        // é o Payment + point_of_interaction
        PaymentPixToOrchestratorResponseDTO paymentPixToOrchestratorResponseDTO = convert.convertPaymentToPaymentPixToOrchestratorResponseDTO(payment, paymentPixResponseDTO);

        // Retornar QR Code para front-end gerar o pagamento
        return paymentPixToOrchestratorResponseDTO;
    }

    public void checkPaymentPixInBackground(Long paymentID) {

        AtomicReference<ScheduledFuture<?>> futureRef = new AtomicReference<>();

        Runnable task = () -> {
            try {
                PaymentPixResponseDTO paymentPixResponseDTO = paymentRequest.getPaymentPix(token, paymentID);

                if ("approved".equals(paymentPixResponseDTO.status())) {

                    // Pagamento aprovado

                    // Cancelar futuras execuções
                    ScheduledFuture<?> future = futureRef.get();
                    if (future != null) {
                        future.cancel(false);

                        // faço requisição para o orquestrador para avisar que deu certo
                        orchestratorPaymentRequest.notify(paymentPixResponseDTO.status());
                    }

                }else{
                    System.out.println("Status "+paymentPixResponseDTO.status()+", tentando novamente em 10 segundos...");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        };

        ScheduledFuture<?> scheduledFuture = scheduler.scheduleAtFixedRate(task, 0, 10, TimeUnit.SECONDS);
        futureRef.set(scheduledFuture);

    }

}
