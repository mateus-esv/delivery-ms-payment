package com.delivery.payment.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.delivery.payment.api.dto.paymentCreditCardRequest.PaymentCreditCardRequestDTO;
import com.delivery.payment.api.dto.paymentCreditCardResponse.PaymentCreditCardResponseDTO;
import com.delivery.payment.api.dto.paymentCreditCardResponse.PaymentCreditCardToOrchestratorResponseDTO;
import com.delivery.payment.api.dto.paymentPixRequest.PaymentPixRequestDTO;
import com.delivery.payment.api.dto.paymentPixResponse.PaymentPixToOrchestratorResponseDTO;
import com.delivery.payment.domain.service.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService service;
    
    @PostMapping("/credit-card")
    public ResponseEntity<PaymentCreditCardToOrchestratorResponseDTO> creditCard(@RequestBody PaymentCreditCardRequestDTO paymentCreditCardRequestDTO){
        return ResponseEntity.ok(service.createCreditCardPayment(paymentCreditCardRequestDTO)); // retorna o codigo pix e o qrcode para pessoa pagar
    }

    @GetMapping("/check-status-credit-card/{id}")
    public ResponseEntity<PaymentCreditCardResponseDTO> checkStatusCreditCard(@PathVariable("id") Long id){
        return ResponseEntity.ok(service.checkPaymentCreditCard(id)); // retorna os dados do pagamento no mercado pago pelo id
    }
    
    @PostMapping("/generate-pix-code")
    public ResponseEntity<PaymentPixToOrchestratorResponseDTO> generatePixCode(@RequestBody PaymentPixRequestDTO paymentPixRequestDTO){
        return ResponseEntity.ok(service.createPIXPayment(paymentPixRequestDTO)); // retorna o codigo pix e o qrcode para pessoa pagar
    }

}
