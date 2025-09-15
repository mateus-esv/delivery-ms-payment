package com.delivery.payment.api.dto.paymentPixResponse;

public record PaymentPixResponseDTO(Long id,
        String status,
        String status_detail,
        PointOfInteraction point_of_interaction
) {}
