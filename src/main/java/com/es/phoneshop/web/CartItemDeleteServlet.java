package com.es.phoneshop.web;

import com.es.phoneshop.model.product.service.CartService;
import com.es.phoneshop.model.product.service.ProductService;
import com.es.phoneshop.model.product.service.impl.CartServiceImpl;
import com.es.phoneshop.model.product.service.impl.ProductServiceImpl;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class CartItemDeleteServlet extends HttpServlet {
    private CartService cartService;
    private ProductService productService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = CartServiceImpl.getInstance();
        productService = ProductServiceImpl.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        cartService.delete(
                cartService.getCart(request),
                productService.parseProductIdFromDeleteOrAddRequest(request),
                request);
        response.sendRedirect(String.format("%s/cart?message=Item was successfully deleted from the cart!", request.getContextPath()));
    }
}
