package com.example.springlab.service;

import com.example.springlab.model.PurchaseOrder;
import com.example.springlab.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LazyLoadingDemoService {

    private final PurchaseOrderRepository purchaseOrderRepository;

    public LazyLoadingDemoService(PurchaseOrderRepository purchaseOrderRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    @Transactional(readOnly = true)
    public PurchaseOrder loadOrder(Long id){
       return  purchaseOrderRepository.findById(id).orElseThrow();
    }
}
