package com.wolo.order.common.exception;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(Long productId) {
        super("Product " + productId + " not found");
    }
}
