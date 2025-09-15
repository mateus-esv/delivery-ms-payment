package com.delivery.payment.api.dto.paymentRequest;

import lombok.Builder;

@Builder
public record AddressDTO(
        String zip_code,
        String street_name,
        String street_number,
        String neighborhood,
        String city,
        String federal_unit
) {}
