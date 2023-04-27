package com.es.phoneshop.model.product;

public class ProductNotFoundException extends RuntimeException{
    private String exeptionMessage;

    public ProductNotFoundException(String exeptionMessage) {
        this.exeptionMessage=exeptionMessage;
    }
}
