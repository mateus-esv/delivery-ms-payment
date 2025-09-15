package com.delivery.payment.api.dto.payer;

import lombok.Builder;

@Builder
public record IdentificationDTO(String type, String number) { 
}
