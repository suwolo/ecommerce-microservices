package com.wolo.order.common.exception;

public class InsufficientStockException extends RuntimeException{
    public InsufficientStockException(Long productId, Long requested, Long available) {
        super("Insufficient stock for product " + productId
                + ": requested " + requested + ", available " + available);
    }
}