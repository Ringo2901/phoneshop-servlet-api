package com.es.phoneshop.model.product.dao.impl;

import com.es.phoneshop.model.product.dao.EntityDao;
import com.es.phoneshop.model.product.enums.SortField;
import com.es.phoneshop.model.product.enums.SortOrder;
import com.es.phoneshop.model.product.model.Product;
import com.es.phoneshop.model.product.exception.ProductNotFoundException;
import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.model.product.service.impl.CartServiceImpl;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
    public List<Product> findProducts(String query, SortField sort, SortOrder order) {
        readWriteLock.readLock().lock();
        try {
            List<Product> result = entities.stream()
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
            return result;
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
