
package com.example.springlab.service;

import com.example.springlab.model.PurchaseOrder;
import com.example.springlab.repository.PurchaseOrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class PropagationDemoService {

    private final PurchaseOrderRepository orderRepository;
    private final PaymentProcessingService paymentService;

    @PersistenceContext
    private EntityManager entityManager;

    public PropagationDemoService(
            PurchaseOrderRepository orderRepository,
            PaymentProcessingService paymentService) {
        this.orderRepository = orderRepository;
        this.paymentService = paymentService;
    }

    @Transactional
    public void updateOrderAndFail(Long orderId) {
        PurchaseOrder order = orderRepository.findById(orderId)
                .orElseThrow();

        order.setTotal(new BigDecimal("888888.88"));
        entityManager.flush();

        paymentService.createPayment(
                orderId,
                new BigDecimal("888888.88")
        );

        throw new IllegalStateException(
                "Simulated outer transaction failure"
        );
    }
}
