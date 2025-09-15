package com.delivery.payment.api.dto.paymentRequest;

import lombok.Builder;

@Builder
public record IdentificationDTO(String type, String number) { 
}
