package com.es.phoneshop.model.product.dao;

import com.es.phoneshop.model.product.enums.SortField;
import com.es.phoneshop.model.product.enums.SortOrder;
import com.es.phoneshop.model.product.model.Product;

import java.util.List;

public interface ProductDao {
    Product getProduct(Long id);

    List<Product> findProducts(String query, SortField sort, SortOrder order);

    void save(Product product);

    void delete(Long id);
}
