package com.delivery.payment.api.dto.paymentPixResponse;

import lombok.Builder;

@Builder
public record PaymentPixResponseDTO(Long id,
        String status,
        String status_detail,
        PointOfInteraction point_of_interaction
) {}
