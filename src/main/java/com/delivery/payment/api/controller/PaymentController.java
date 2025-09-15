package com.delivery.payment.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.delivery.payment.api.dto.paymentRequest.PaymentCreditCardMSRequestDTO;
import com.delivery.payment.domain.service.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService service;

    
    @PostMapping("/credit-card")
    public void creditCard(@RequestBody PaymentCreditCardMSRequestDTO paymentCreditCardMSRequestDTO){
        System.out.println(paymentCreditCardMSRequestDTO.toString());
        service.createCreditCardPayment(paymentCreditCardMSRequestDTO);
    }
    
    @PostMapping("/pix")
    public void pix(){
        service.createPIXPayment();
    }

}
