package com.example.springlab.repository;

import com.example.springlab.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    long countByAmount(BigDecimal amount);
}