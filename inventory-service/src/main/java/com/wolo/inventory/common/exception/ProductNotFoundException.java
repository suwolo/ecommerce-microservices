package com.wolo.inventory.common.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long id) {
        super("Product " + id + " not found");
    }
}
