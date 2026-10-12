package com.example.springlab.model;


import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name="purchase_orders")
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 12, scale =2)
    private BigDecimal total;

    @ManyToOne(fetch= FetchType.LAZY, optional = false)
    @JoinColumn(name= "customer_id", nullable = false)
    private Customer customer;

    protected PurchaseOrder() {}

    public PurchaseOrder(BigDecimal total, Customer customer) {
        this.total = total;
        this.customer = customer;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
