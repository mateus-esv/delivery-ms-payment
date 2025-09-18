package com.delivery.payment.domain.request;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.delivery.payment.api.dto.paymentCreditCardRequest.PaymentCreditCardMercadoPagoRequestDTO;
import com.delivery.payment.api.dto.paymentCreditCardResponse.PaymentCreditCardResponseDTO;
import com.delivery.payment.api.dto.paymentPixRequest.PaymentPixMercadoPagoRequestDTO;
import com.delivery.payment.api.dto.paymentPixResponse.PaymentPixResponseDTO;

@FeignClient(name = "payment-mercadopago-request", url = "${url.base.mercado-pago}")
public interface PaymentRequest {

        @PostMapping("/v1/payments")
        public PaymentCreditCardResponseDTO createCreditCardPayment(@RequestHeader("Authorization") String bearerToken,
                        @RequestHeader("X-Idempotency-Key") String idempotencyKey,
                        @RequestBody PaymentCreditCardMercadoPagoRequestDTO paymentCreditCardMercadoPagoRequestDTO);

        @PostMapping("/v1/payments")
        public PaymentPixResponseDTO createPIXPayment(@RequestHeader("Authorization") String bearerToken,
                        @RequestHeader("X-Idempotency-Key") String idempotencyKey,
                        @RequestBody PaymentPixMercadoPagoRequestDTO paymentPixMercadoPagoRequestDTO);

        @GetMapping("/v1/payments/{id}")
        public PaymentCreditCardResponseDTO getPaymentCreditCard(
                        @RequestHeader("Authorization") String bearerToken,
                        @PathVariable("id") Long paymentId);

                        
        @GetMapping("/v1/payments/{id}")
        public PaymentPixResponseDTO getPaymentPix(
                        @RequestHeader("Authorization") String bearerToken,
                        @PathVariable("id") Long paymentId);


}
