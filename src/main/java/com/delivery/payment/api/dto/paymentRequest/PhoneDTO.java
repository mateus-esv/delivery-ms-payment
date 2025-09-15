package com.delivery.payment.api.dto.paymentRequest;

import lombok.Builder;

@Builder
public record PhoneDTO(String area_code, String number) {
}
