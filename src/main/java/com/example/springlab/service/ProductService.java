package com.example.springlab.service;

import com.example.springlab.model.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductService {

    private final Map<Long, Product> products =
            new ConcurrentHashMap<>();

    private final AtomicLong idGenerator = new AtomicLong();

    public ProductService() {
        create("Keyboard", new BigDecimal("2499.00"));
        create("Mouse", new BigDecimal("799.00"));
    }

    public List<Product> findAll() {
        return List.copyOf(products.values());
    }

    public Product findById(Long id) {
        return products.get(id);
    }

    public Product create(String name, BigDecimal price) {
        Long id = idGenerator.incrementAndGet();

        Product product = new Product(id, name, price);
        products.put(id, product);

        return product;
    }

    public Product update(
            Long id, String name, BigDecimal price) {

        if (!products.containsKey(id)) {
            return null;
        }

        Product updated = new Product(id, name, price);
        products.put(id, updated);

        return updated;
    }

    public boolean delete(Long id) {
        return products.remove(id) != null;
    }
}