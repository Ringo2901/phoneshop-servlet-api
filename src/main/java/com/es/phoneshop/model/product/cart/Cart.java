package com.es.phoneshop.model.product.cart;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Cart {
    private List<CartItem> items;
    private int cartId;

    public Cart(int cartId) {
        items = new ArrayList<>();
        this.cartId = cartId;
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
}
