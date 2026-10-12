
package com.example.springlab.service;

import com.example.springlab.model.Payment;
import com.example.springlab.model.PurchaseOrder;
import com.example.springlab.repository.PaymentRepository;
import com.example.springlab.repository.PurchaseOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class PaymentProcessingService {

    private final PurchaseOrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    public PaymentProcessingService(
            PurchaseOrderRepository orderRepository,
            PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createPayment(Long orderId, BigDecimal amount) {
        PurchaseOrder order = orderRepository.findById(orderId)
                .orElseThrow();

        Payment payment = new Payment(amount, order);
        paymentRepository.saveAndFlush(payment);
    }
}
