package com.example.springlab.dto;

import java.math.BigDecimal;

public record OrderSummaryDto(
        Long orderId,
        BigDecimal total,
        String customerName
) {}