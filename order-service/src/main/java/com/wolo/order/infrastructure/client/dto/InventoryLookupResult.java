package com.wolo.order.infrastructure.client.dto;

public record InventoryLookupResult(
        InventoryResponse inventoryResponse,
        boolean serviceAvailable
) {
}
