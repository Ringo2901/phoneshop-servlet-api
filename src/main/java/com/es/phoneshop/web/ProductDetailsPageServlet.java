package com.es.phoneshop.web;

import com.es.phoneshop.model.product.cart.Cart;
import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.model.product.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.product.exception.OutOfStockException;
import com.es.phoneshop.model.product.model.Product;
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
import java.text.NumberFormat;
import java.text.ParseException;

public class ProductDetailsPageServlet extends HttpServlet {
    private ProductDao productDao;
    private CartService cartService;
    private ProductService productService;
    private static final String PRICE_HISTORY_PAGE = "/WEB-INF/pages/priceHistory.jsp";
    private static final String PRODUCT_PAGE = "/WEB-INF/pages/productPage.jsp";
    private static final String PRODUCTS_ATTRIBUTE = "product";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = CartServiceImpl.getInstance();
        productService = ProductServiceImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getPathInfo().substring(1).contains("/")) {
            request.setAttribute(PRODUCTS_ATTRIBUTE, productDao.getProduct(productService.parseIdWithHistory(request)));
            request.getRequestDispatcher(PRICE_HISTORY_PAGE).forward(request, response);
        } else {
            Product product = productDao.getProduct(productService.parseIdWithoutHistory(request));
            request.setAttribute(PRODUCTS_ATTRIBUTE, product);
            productService.addProductToRecentProducts(product, request);
            request.getRequestDispatcher(PRODUCT_PAGE).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long productId = productService.parseIdWithoutHistory(request);
        int quantity;
        Cart cart = cartService.getCart(request);
        try {
            if (!request.getParameter("quantity").matches("^\\d+([\\.\\,]\\d+)?$")) {
                throw new ParseException("Not a number!", 0);
            }
            NumberFormat numberFormat = NumberFormat.getNumberInstance(request.getLocale());
            quantity = numberFormat.parse(request.getParameter("quantity")).intValue();
        } catch (ParseException e) {
            request.setAttribute("error", "Not a number!");
            response.sendRedirect(request.getContextPath() + "/products/" + productId + "?message=&error=Not a number!");
            return;
        }
        try {
            cartService.add(cart, productId, quantity, request);
        } catch (OutOfStockException e) {
            request.setAttribute("error", "Not enought items in the stock! Available: " + e.getAvailableStock());
            response.sendRedirect(request.getContextPath() + "/products/" + productId + "?message=&error=Not enough items in stock! Available: " + e.getAvailableStock());
            return;
        }
        request.setAttribute("product", productDao.getProduct(productId));
        response.sendRedirect(request.getContextPath() + "/products/" + productId + "?message=Product was added to cart successfully!");
    }
}
