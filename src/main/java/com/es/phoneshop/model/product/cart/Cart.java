package com.es.phoneshop.model.product.cart;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cart implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<CartItem> items;
    private int cartId;
    private int totalItems;
    private BigDecimal totalCost;

    public Cart(int cartId) {
        items = new ArrayList<>();
        this.cartId = cartId;
    }

    public Cart(){

    }

    public List<CartItem> getItems() {
        return items;
    }

    public int getCartId() {
        return cartId;
    }

    @Override
    public String toString() {
        return "Cart{" +
                items +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return items.equals(cart.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items);
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
}
