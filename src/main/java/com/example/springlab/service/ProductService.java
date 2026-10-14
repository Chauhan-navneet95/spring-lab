package com.example.springlab.service;

import com.example.springlab.model.Product;
import com.example.springlab.repository.ProductRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @PreAuthorize("hasRole('USER')")
    public Product create(String name, BigDecimal price) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String ownerUsername = authentication.getName();

        Product product = new Product(null, name, price,ownerUsername);
        return productRepository.save(product);
    }

    @PreAuthorize(
            "@productAuthorization.canEdit(authentication, #id)"
    )
    public Product update(Long id, String name, BigDecimal price) {
        Product product = productRepository.findById(id).orElse(null);

        if (product == null) {
            return null;
        }

        product.setName(name);
        product.setPrice(price);

        return productRepository.save(product);
    }

    public boolean delete(Long id) {
        if (!productRepository.existsById(id)) {
            return false;
        }

        productRepository.deleteById(id);
        return true;
    }
}