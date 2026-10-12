package com.example.springlab.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name ="customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    protected Customer(){}

    public Customer(String name){
        this.name = name;
    }

    public Long getId() {
        return id;
    }
     public String getName() {
        return name;
     }
}
