package com.es.phoneshop.model.product.dao.impl;

import com.es.phoneshop.model.product.enums.SortField;
import com.es.phoneshop.model.product.enums.SortOrder;
import com.es.phoneshop.model.product.model.Product;
import com.es.phoneshop.model.product.exception.ProductNotFoundException;
import com.es.phoneshop.model.product.dao.ProductDao;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {
    private static ProductDao instance;
    private List<Product> products;
    private long currentId = 1;
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();


    private ArrayListProductDao() {
        products = new ArrayList<Product>();
    }

    public static synchronized ProductDao getInstance() {
        if (instance == null) {
            instance = new ArrayListProductDao();
        }
        return instance;
    }

    @Override
    public Product getProduct(Long id) {
        if (id != null) {
            readWriteLock.readLock().lock();
            Product product = products.stream()
                    .filter(product1 -> id.equals(product1.getId()))
                    .findAny()
                    .orElseThrow(() -> new ProductNotFoundException("Product with id = " + id + " was not found"));
            readWriteLock.readLock().unlock();
            return product;
        } else {
            throw new IllegalArgumentException("Id is null");
        }
    }

    @Override
    public List<Product> findProducts(String query, SortField sort, SortOrder order) {
        readWriteLock.readLock().lock();
        List<Product> result = products.stream()
                .filter(Objects::nonNull)
                .filter(product -> query == null || query.isEmpty() || isContainAnyWord(query, product.getDescription()))
                .sorted((p1, p2) -> {
                    if (query == null) {
                        return 0;
                    }
                    int firstNumberOfContains = numberOfContainWords(query, p1.getDescription());
                    int secondNumberOfContains = numberOfContainWords(query, p2.getDescription());
                    return secondNumberOfContains - firstNumberOfContains;
                })
                .filter(product -> product.getPrice() != null)
                .filter((product -> product.getStock() > 0))
                .toList();
        if (sort != null && order != null) {
            Comparator<Product> comparator = Comparator.comparing(product ->
                    chooseSortField(product, sort)
            );
            if (SortOrder.ASC == order) {
                comparator = comparator.thenComparing(Product::getDescription);
            } else {
                comparator = comparator.thenComparing(Product::getDescription).reversed();
            }
            result = result.stream()
                    .sorted(comparator)
                    .toList();
        }
        readWriteLock.readLock().unlock();
        return result;
    }

    @Override
    public void save(Product product) {
        readWriteLock.writeLock().lock();
        if (product.getId() != null) {
            products.stream()
                    .filter(product1 -> product1.getId().equals(product.getId()))
                    .findAny()
                    .ifPresentOrElse(product1 -> {
                                products.set(products.indexOf(product1), product);
                            },
                            () -> {
                                products.add(product);
                            });
        } else {
            product.setId(currentId++);
            products.add(product);
        }
        readWriteLock.writeLock().unlock();
    }

    @Override
    public void delete(Long id) {
        if (id != null) {
            readWriteLock.writeLock().lock();
            products.removeIf(product -> id.equals(product.getId()));
            readWriteLock.writeLock().unlock();
        } else {
            throw new IllegalArgumentException("Id is null");
        }
    }

    private boolean isContainAnyWord(String query, String desc) {
        String[] words = query.split(" +");
        for (String word : words) {
            if (desc.contains(word)) {
                return true;
            }
        }
        return false;
    }

    private int numberOfContainWords(String query, String desc) {
        int counter = 0;
        String[] words = query.split(" +");
        for (String word : words) {
            if (desc.contains((word))) {
                counter++;
            }
        }
        return counter;
    }

    private Comparable chooseSortField(Product product, SortField sortField) {
        if (SortField.DESCRIPTION == sortField) {
            return product.getDescription();
        } else {
            return product.getPrice();
        }
    }


}
