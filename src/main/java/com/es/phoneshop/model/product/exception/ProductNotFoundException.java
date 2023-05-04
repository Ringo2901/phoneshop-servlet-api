package com.es.phoneshop.model.product.exception;

public class ProductNotFoundException extends RuntimeException {
    private String exeptionMessage;

    public ProductNotFoundException(String exeptionMessage) {
        this.exeptionMessage = exeptionMessage;
    }
}
