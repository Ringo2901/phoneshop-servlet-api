package com.es.phoneshop.web;

import com.es.phoneshop.model.product.dao.impl.ArrayListProductDao;

import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.model.product.enums.SortField;
import com.es.phoneshop.model.product.enums.SortOrder;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

public class ProductListPageServlet extends HttpServlet {
    private ProductDao productDao;
    private static final String QUERY_PARAMETER = "query";
    private static final String SORT_PARAMETER = "sort";
    private static final String ORDER_PARAMETER = "order";
    private static final String PRODUCTS_ATTRIBUTE = "products";
    private static final String PRODUCT_LIST_JSP = "/WEB-INF/pages/productList.jsp";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(PRODUCTS_ATTRIBUTE, productDao.findProducts(
                request.getParameter(QUERY_PARAMETER),
                Optional.ofNullable(request.getParameter(SORT_PARAMETER)).map(SortField::valueOf).orElse(null),
                Optional.ofNullable(request.getParameter(ORDER_PARAMETER)).map(SortOrder::valueOf).orElse(null)));
        request.getRequestDispatcher(PRODUCT_LIST_JSP).forward(request, response);
    }

}
