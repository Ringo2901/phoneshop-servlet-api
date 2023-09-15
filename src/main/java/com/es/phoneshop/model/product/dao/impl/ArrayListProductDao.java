package com.es.phoneshop.model.product.dao.impl;

import com.es.phoneshop.model.product.dao.EntityDao;
import com.es.phoneshop.model.product.enums.SortField;
import com.es.phoneshop.model.product.enums.SortOrder;
import com.es.phoneshop.model.product.model.Product;
import com.es.phoneshop.model.product.dao.ProductDao;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class ArrayListProductDao extends EntityDao<Product> implements ProductDao {
    private static volatile ProductDao instance;
    private long currentMaxId = 1;
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();


    private ArrayListProductDao() {
        entities = new ArrayList<>();
    }

    public static ProductDao getInstance() {
        if (instance == null) {
            synchronized (ProductDao.class) {
                if (instance == null) {
                    instance = new ArrayListProductDao();
                }
            }
        }
        return instance;
    }


    @Override
    public List<Product> findProducts(String query, SortField sort, SortOrder order, boolean lazySearch) {
        readWriteLock.readLock().lock();
        List<Product> foundProducts;
        try {
            if (!lazySearch) {
                foundProducts = entities.stream()
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
                    foundProducts = foundProducts.stream()
                            .sorted(comparator)
                            .toList();
                }
            } else {
                foundProducts = entities.stream()
                        .filter(Objects::nonNull)
                        .filter(product -> containsAllWordFrom(query, product.getDescription()))
                        .filter(product -> product.getPrice() != null)
                        .filter(product -> product.getStock() > 0)
                        .collect(Collectors.toList());

            }
            return foundProducts;
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    protected final void addNewEntity(Product entity, List<Product> entities) {
        entity.setId(currentMaxId++);
        entities.add(entity);
    }

    @Override
    public void delete(Long id) {
        if (id != null) {
            readWriteLock.writeLock().lock();
            try {
                entities.removeIf(product -> id.equals(product.getId()));
            } finally {
                readWriteLock.writeLock().unlock();
            }
        } else {
            throw new IllegalArgumentException("Id is null");
        }
    }

    private boolean isContainAnyWord(String query, String desc) {
        String[] words = query.split(" +");
        for (String word : words) {
            if (desc.toLowerCase().contains(word.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private boolean containsAllWordFrom(String query, String desc) {
        String[] words = query.split(" +");
        for (String word : words)
            if (!desc.toLowerCase().contains(word.toLowerCase())) {
                return false;
            }
        return true;
    }

    private int numberOfContainWords(String query, String desc) {
        int counter = 0;
        String[] words = query.split(" +");
        for (String word : words) {
            if (desc.toLowerCase().contains(word.toLowerCase())) {
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
