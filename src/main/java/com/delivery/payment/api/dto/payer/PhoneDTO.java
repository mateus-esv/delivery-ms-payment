package com.delivery.payment.api.dto.payer;

import lombok.Builder;

@Builder
public record PhoneDTO(String area_code, String number) {
}
