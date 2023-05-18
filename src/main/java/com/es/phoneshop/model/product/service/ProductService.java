package com.es.phoneshop.model.product.service;

import com.es.phoneshop.model.product.model.Product;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Set;

public interface ProductService {
    Long parseIdWithHistory(HttpServletRequest request) throws NumberFormatException;

    Long parseIdWithoutHistory(HttpServletRequest request) throws NumberFormatException;

    void addProductToRecentProducts(Product product, HttpServletRequest request);

    void setRecentProductsInSession(HttpServletRequest request, Set<Product> products);
}
