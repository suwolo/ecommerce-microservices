package com.wolo.order.api.dto;

public record OrdersResponse(
        Long customerId,
        Long productId,
        Long quantity,
        String status
) {
}
