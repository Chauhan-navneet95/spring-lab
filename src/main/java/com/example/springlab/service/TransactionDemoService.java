
package com.example.springlab.service;

import com.example.springlab.model.Customer;
import com.example.springlab.model.Payment;
import com.example.springlab.model.PurchaseOrder;
import com.example.springlab.repository.CustomerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransactionDemoService {

    private final CustomerRepository customerRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public TransactionDemoService(
            CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public void createOrderAndPaymentThenFail(Long customerId) {

        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow();

        PurchaseOrder order = new PurchaseOrder(
                new BigDecimal("777777.77"),
                customer
        );

        entityManager.persist(order);
        entityManager.flush();

        Payment payment = new Payment(
                new BigDecimal("777777.77"),
                order
        );

        entityManager.persist(payment);
        entityManager.flush();

        // Deliberately fail after both writes have been flushed.
        throw new IllegalStateException(
                "Simulated failure after saving order and payment"
        );
    }
}
