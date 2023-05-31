package com.es.phoneshop.model.product.model;

import java.io.Serializable;
import java.util.Objects;

public class Entity implements Serializable {
    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
