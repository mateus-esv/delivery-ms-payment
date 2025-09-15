package com.delivery.payment.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.delivery.payment.comuns.enums.PaymentMethod;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Payment {
    
    @Id
    private UUID id;
    private UUID orderIDFK; // id do pedido (chave estrangeira)
    private UUID customerIDFK; // id do cliente (chave estrangeira)

    private LocalDateTime paymentDate; // data do pagamento
    private PaymentMethod paymentMethod; // método de pagamento (pix, cartão de crédito)
        
    private Long idPaymentMercadoPago; // id do pagamento no mercado pago
    private String statusPaymentMercadoPago; // status do pagamento no mercado pago
    private String statusDetailPaymentMercadoPago; // detalhe do status do pagamento no mercado pago
    
    private BigDecimal transactionAmount; // valor da transação

}
