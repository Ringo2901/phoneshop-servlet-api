package com.es.phoneshop.model.product.service.impl;

import com.es.phoneshop.model.product.cart.Cart;
import com.es.phoneshop.model.product.cart.CartItem;
import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.model.product.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.product.exception.OutOfStockException;
import com.es.phoneshop.model.product.model.Product;
import com.es.phoneshop.model.product.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class CartServiceImpl implements CartService {
    private static final String CART_SESSION_ATTRIBUTE = CartServiceImpl.class.getName() + ".cart";
    private static CartServiceImpl instance;
    private ProductDao productDao;

    public static CartServiceImpl getInstance() {
        if (instance == null) {
            synchronized (CartServiceImpl.class) {
                if (instance == null) {
                    instance = new CartServiceImpl();
                }
            }
        }
        return instance;
    }

    private CartServiceImpl() {
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    public Cart getCart(HttpServletRequest request) {
        HttpSession currentSession = request.getSession();
        synchronized (currentSession) {
            Cart cart = (Cart) currentSession.getAttribute(CART_SESSION_ATTRIBUTE);
            if (cart == null) {
                cart = new Cart();
                currentSession.setAttribute(CART_SESSION_ATTRIBUTE, cart);
            }
            return cart;
        }
    }

    @Override
    public void add(Cart cart, Long productId, int quantity, HttpServletRequest request) throws OutOfStockException {
        HttpSession currentSession = request.getSession();
        synchronized (currentSession) {
            Product product = productDao.getProduct(productId);
            if (countingQuantityIncludingCart(cart, product) < quantity) {
                throw new OutOfStockException(product, quantity, product.getStock());
            }
            CartItem productMatch = cart.getItems().stream()
                    .filter(currProduct -> currProduct.getProduct().equals(product))
                    .findFirst()
                    .orElse(null);
            if (productMatch != null) {
                int index = cart.getItems().indexOf(productMatch);
                CartItem item = cart.getItems().get(index);
                item.setQuantity(item.getQuantity() + quantity);
            } else {
                cart.getItems().add(new CartItem(product, quantity));
            }
        }
    }

    private int countingQuantityIncludingCart(Cart cart, Product product) {
        int result = product.getStock();
        Integer quantityInCart = cart.getItems().stream()
                .filter(currProduct -> currProduct.getProduct().equals(product))
                .map(CartItem::getQuantity)
                .findFirst()
                .orElse(0);
        result -= quantityInCart;
        return result;
    }

}
