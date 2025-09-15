// package com.delivery.payment.api.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import com.delivery.payment.domain.service.PaymentService;

// import java.util.Map;

// @RestController
// @RequestMapping("/api/webhook/mercadopago")
// public class MercadoPagoWebhookController {

//     @Autowired
//     private PaymentService service;

//     @PostMapping
//     public ResponseEntity<String> receiveNotification(@RequestBody Map<String, Object> payload) {
//         // Aqui você recebe o JSON enviado pelo Mercado Pago
//         // O payload contém informações como:
//         // "id", "type" (payment, merchant_order), "date_created", etc.

//         System.out.println("Notificação recebida do Mercado Pago: " + payload);

//         String type = (String) payload.get("type");

//         if ("payment".equals(type)) {
//             processarPagamento(payload);
//         } 

//         // Sempre retorna 200 OK para o Mercado Pago saber que a notificação foi recebida
//         return ResponseEntity.ok(null);
//     }

//     private void processarPagamento(Map<String, Object> payload) {
//         // Extrair id do pagamento
//         String paymentId = (String) payload.get("id");

//         // devemos conferir o status do pagamento, pois pode ter sido rejeitado
//         // além disso, é uma forma de assegurar de que quem chamou esse endpoint foi de fato o mercado pago e não alguem mal intencionado mentindo que o pagamento foi concluido sem ter sido de fato
//         // dessa forma, fazemos requisição no mercado pago para confirmar o status

//         service.checkPayment(paymentId);

//     }

// }
