package com.es.phoneshop.model.product.service;

import com.es.phoneshop.model.product.model.Product;
import jakarta.servlet.http.HttpServletRequest;

import java.text.ParseException;
import java.util.Set;

public interface ProductService {
    Long parseIdWithHistory(HttpServletRequest request) throws NumberFormatException;

    Long parseIdWithoutHistory(HttpServletRequest request) throws NumberFormatException;

    void addProductToRecentProducts(Product product, HttpServletRequest request);
    Long parseProductIdFromDeleteOrAddRequest(HttpServletRequest request) throws NumberFormatException;
    void setRecentProductsInSession(HttpServletRequest request, Set<Product> products);
    int parseQuantity(String quantity, HttpServletRequest request) throws ParseException;

}
