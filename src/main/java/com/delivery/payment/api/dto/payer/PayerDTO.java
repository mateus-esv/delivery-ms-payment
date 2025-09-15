package com.delivery.payment.api.dto.payer;

import lombok.Builder;

@Builder
public record PayerDTO(String first_name, String last_name, String email, PhoneDTO phone, IdentificationDTO identification, AddressDTO address) {}
