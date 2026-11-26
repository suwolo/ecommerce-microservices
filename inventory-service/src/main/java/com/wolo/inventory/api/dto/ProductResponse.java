package com.wolo.inventory.api.dto;

public record ProductResponse(
        Long productId,
        String name,
        Long stock,
        boolean available
) {
}
