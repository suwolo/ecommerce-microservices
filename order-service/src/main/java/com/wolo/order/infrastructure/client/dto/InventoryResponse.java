package com.wolo.order.infrastructure.client.dto;

public record InventoryResponse(
        Long productId,
        String name,
        Long stock,
        boolean available
) {
}
