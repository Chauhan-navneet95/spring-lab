package com.example.springlab.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateProductRequest(

        @NotBlank(message = "Product name is required")
        String name,

        @NotNull(message = "Product price is required")
        @Positive(message = "Product price must be positive")
        BigDecimal price

) {}