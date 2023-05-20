package com.es.phoneshop.model.product.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class PriceHistory implements Serializable {
    private String date;
    private BigDecimal price;

    public String getDate() {
        return date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public PriceHistory(String date, BigDecimal price) {
        this.date = date;
        this.price = price;
    }
}
