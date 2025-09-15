package com.delivery.payment.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.delivery.payment.api.dto.paymentCreditCardRequest.PaymentCreditCardRequestDTO;
import com.delivery.payment.api.dto.paymentPixRequest.PaymentPixRequestDTO;
import com.delivery.payment.api.dto.paymentPixResponse.PaymentPixResponseDTO;
import com.delivery.payment.domain.service.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService service;
    
    @PostMapping("/credit-card")
    public void creditCard(@RequestBody PaymentCreditCardRequestDTO paymentCreditCardMSRequestDTO){
        service.createCreditCardPayment(paymentCreditCardMSRequestDTO);
    }
    
    @PostMapping("/generate-pix-code")
    public ResponseEntity<PaymentPixResponseDTO> generatePixCode(@RequestBody PaymentPixRequestDTO paymentPixRequestDTO){
        return ResponseEntity.ok(service.createPIXPayment(paymentPixRequestDTO)); // retorna o codigo pix e o qrcode para pessoa pagar
    }

}
