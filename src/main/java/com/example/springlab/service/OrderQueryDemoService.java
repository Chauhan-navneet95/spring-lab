package com.example.springlab.service;

import com.example.springlab.dto.OrderSummaryDto;
import com.example.springlab.model.PurchaseOrder;
import com.example.springlab.repository.PurchaseOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderQueryDemoService {

    private final PurchaseOrderRepository purchaseOrderRepository;

    public OrderQueryDemoService(PurchaseOrderRepository purchaseOrderRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    public record OrderSummary(
            Long orderId,
            BigDecimal total,
            String customerName
    ){}

    @Transactional(readOnly = true)
    public List<OrderSummary> findAllWithNPlusOne(){
        List<PurchaseOrder> orders= purchaseOrderRepository.findAll();

        return orders.stream().map(order -> new OrderSummary(
                order.getId(),
                order.getTotal(),
                order.getCustomer().getName()
        )).toList();
    }

    @Transactional(readOnly = true)
    public List<OrderSummary> findAllWithFetchJoin(){
        List<PurchaseOrder> orders = purchaseOrderRepository.findAllWithCustomer();
        return orders.stream().map(order -> new OrderSummary(
                order.getId(),
                order.getTotal(),
                order.getCustomer().getName()
        )).toList();
    }


    @Transactional(readOnly = true)
    public List<OrderSummaryDto> findAllSummaries() {
        return purchaseOrderRepository.findAllSummaries();
    }

}
