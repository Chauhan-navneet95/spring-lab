package com.example.springlab.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "purchase_order_id", nullable = false)
    private PurchaseOrder purchaseOrder;

    protected Payment() {
    }

    public Payment(BigDecimal amount, PurchaseOrder purchaseOrder) {
        this.amount = amount;
        this.purchaseOrder = purchaseOrder;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}