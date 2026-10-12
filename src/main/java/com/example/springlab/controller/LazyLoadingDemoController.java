package com.example.springlab.controller;

import com.example.springlab.model.Customer;
import com.example.springlab.model.PurchaseOrder;
import com.example.springlab.service.LazyLoadingDemoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lab/lazy-loading")
public class LazyLoadingDemoController {

    private final LazyLoadingDemoService lazyLoadingDemoService;

    public LazyLoadingDemoController(LazyLoadingDemoService lazyLoadingDemoService) {
        this.lazyLoadingDemoService = lazyLoadingDemoService;
    }

    @GetMapping("/{id}")
    public String demonstrate(@PathVariable Long id){
        PurchaseOrder  purchaseOrder = lazyLoadingDemoService.loadOrder(id);

        // Deliberately access the lazy association
        // after the service transaction has ended.
        return purchaseOrder.getCustomer().getName();
    }
}
