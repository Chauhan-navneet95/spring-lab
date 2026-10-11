package com.example.springlab.service;

import com.example.springlab.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService();
    }

    @Test
    void shouldReturnSeededProducts() {
        assertEquals(2, productService.findAll().size());
    }

    @Test
    void shouldCreateProductWithGeneratedId() {
        Product product = productService.create(
                "Monitor", new BigDecimal("12999.00"));

        assertEquals(3L, product.id());
        assertEquals("Monitor", product.name());
        assertEquals(new BigDecimal("12999.00"), product.price());

        assertEquals(product, productService.findById(3L));
    }

    @Test
    void shouldUpdateExistingProduct() {
        Product updated = productService.update(
                1L, "Mechanical Keyboard",
                new BigDecimal("3499.00"));

        assertNotNull(updated);
        assertEquals("Mechanical Keyboard", updated.name());
        assertEquals(updated, productService.findById(1L));
    }

    @Test
    void shouldReturnNullWhenUpdatingMissingProduct() {
        Product updated = productService.update(
                999L, "Unknown", BigDecimal.TEN);

        assertNull(updated);
    }

    @Test
    void shouldDeleteExistingProduct() {
        assertTrue(productService.delete(1L));
        assertNull(productService.findById(1L));
        assertFalse(productService.delete(1L));
    }
}