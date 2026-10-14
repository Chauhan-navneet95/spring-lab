package com.example.springlab.security;

import com.example.springlab.repository.ProductRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("productAuthorization")
public class ProductAuthorization {

    private final ProductRepository productRepository;

    public ProductAuthorization(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public boolean canEdit(
            Authentication authentication,
            Long productId) {

        return productRepository.findById(productId)
                .map(product ->
                        product.getOwnerUsername()
                                .equals(authentication.getName()))
                .orElse(false);
    }
}