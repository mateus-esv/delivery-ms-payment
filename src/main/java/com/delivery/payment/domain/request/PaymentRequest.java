package com.delivery.payment.domain.request;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.delivery.payment.api.dto.PaymentResponseDTO;
import com.delivery.payment.api.dto.paymentRequest.PaymentCreditCardMercadoPagoRequestDTO;

@FeignClient(name = "payment-mercadopago-request", url = "${url.base}")
public interface PaymentRequest {

        @PostMapping("/v1/payments")
        public PaymentResponseDTO createCreditCardPayment(@RequestHeader("Authorization") String bearerToken,
                        @RequestHeader("X-Idempotency-Key") String idempotencyKey,
                        @RequestBody PaymentCreditCardMercadoPagoRequestDTO paymentCreditCardMercadoPagoRequestDTO);

        @GetMapping("/v1/payments/{id}")
        public PaymentResponseDTO getPayment(
                        @RequestHeader("Authorization") String bearerToken,
                        @PathVariable("id") String paymentId);

}
