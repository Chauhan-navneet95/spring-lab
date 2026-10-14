package com.example.springlab.service;

import com.example.springlab.model.Product;
import com.example.springlab.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void shouldCreateProduct() {
        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> {
                    Product product = invocation.getArgument(0);
                    return new Product(
                            1L, product.getName(), product.getPrice(), product.getOwnerUsername());
                });

        Product created = productService.create(
                "Monitor", new BigDecimal("12999.00"));

        assertEquals(1L, created.getId());
        assertEquals("Monitor", created.getName());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void shouldReturnNullWhenProductDoesNotExist() {
        when(productRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertNull(productService.findById(999L));
    }
}