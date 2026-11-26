package com.wolo.order.common.exception;

public class InventoryUnavailableException extends RuntimeException{
    public InventoryUnavailableException() {
        super("Cannot create order because Inventory Service is unavailable");
    }
}
