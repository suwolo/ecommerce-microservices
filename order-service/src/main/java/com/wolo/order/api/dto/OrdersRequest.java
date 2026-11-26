package com.wolo.order.api.dto;

import jakarta.validation.constraints.NotNull;

public record OrdersRequest(
        @NotNull
        Long customerId,

        @NotNull
        Long productId,

        @NotNull
        Long quantity
) {
}
