package com.es.phoneshop.web;

import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.model.product.dao.impl.ArrayListProductDao;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ProductDetailsPageServlet extends HttpServlet {
    private ProductDao productDao;
    private static final String PRICE_HISTORY_PAGE = "/WEB-INF/pages/priceHistory.jsp";
    private static final String PRODUCT_PAGE = "/WEB-INF/pages/productPage.jsp";
    private static final String PRODUCTS_ATTRIBUTE = "products";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getPathInfo().substring(1).contains("/")) {
            request.setAttribute(PRODUCTS_ATTRIBUTE, productDao.getProduct(
                    Long.parseLong(request.getPathInfo().substring(1, request.getPathInfo().lastIndexOf('/')))));
            request.getRequestDispatcher(PRICE_HISTORY_PAGE).forward(request, response);
        } else {
            request.setAttribute(PRODUCTS_ATTRIBUTE, productDao.getProduct(
                    Long.parseLong(request.getPathInfo().substring(1))));
            request.getRequestDispatcher(PRODUCT_PAGE).forward(request, response);
        }
    }
}
