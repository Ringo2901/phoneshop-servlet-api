package com.es.phoneshop.model.product.service.impl;

import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.model.product.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.product.model.Product;
import com.es.phoneshop.model.product.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.LinkedHashSet;
import java.util.Set;

public class ProductServiceImpl implements ProductService {
    private static ProductServiceImpl instance;
    private Set<Product> recentProducts;

    public static ProductServiceImpl getInstance() {
        if (instance == null) {
            synchronized (ProductServiceImpl.class) {
                if (instance == null) {
                    instance = new ProductServiceImpl();
                }
            }
        }
        return instance;
    }

    private ProductServiceImpl() {

    }

    @Override
    public Long parseIdWithHistory(HttpServletRequest request) throws NumberFormatException {
        return Long.parseLong(request.getPathInfo().substring(1, request.getPathInfo().lastIndexOf('/')));
    }

    @Override
    public Long parseIdWithoutHistory(HttpServletRequest request) throws NumberFormatException {
        return Long.parseLong(request.getPathInfo().substring(1));
    }

    @Override
    public void addProductToRecentProducts(Product product, HttpServletRequest request) {
        if (recentProducts == null) {
            recentProducts = new LinkedHashSet<>();
        }
        if (recentProducts.size() > 2 && !recentProducts.contains((product))) {
            recentProducts.stream()
                    .findFirst()
                    .ifPresent(recentProducts::remove);
        }
        recentProducts.add(product);
        setRecentProductsInSession(request, recentProducts);
    }

    @Override
    public void setRecentProductsInSession(HttpServletRequest request, Set<Product> products) {
        HttpSession currentSession = request.getSession();
        currentSession.setAttribute("recentProducts", products);
    }
}
