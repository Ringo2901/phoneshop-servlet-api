package com.es.phoneshop.web;

import com.es.phoneshop.model.product.service.CartService;
import com.es.phoneshop.model.product.service.impl.CartServiceImpl;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class MiniCartServlet extends HttpServlet {
    private CartService cartService;
    private static final String MINI_CART_JSP = "/WEB-INF/pages/miniCart.jsp";
    private static final String CART_ATTRIBUTE = "cart";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = CartServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(CART_ATTRIBUTE, cartService.getCart(request));
        request.getRequestDispatcher(MINI_CART_JSP).include(request, response);
    }
}
