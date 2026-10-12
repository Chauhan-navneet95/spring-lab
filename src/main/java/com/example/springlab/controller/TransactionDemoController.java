package com.example.springlab.controller;

import com.example.springlab.service.TransactionDemoService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lab/transactions")
public class TransactionDemoController {

    private TransactionDemoService transactionDemoService;

    public TransactionDemoController(TransactionDemoService transactionDemoService) {
        this.transactionDemoService = transactionDemoService;
    }

    @PostMapping("/rollback/{customerId}")
    public String rollback(@PathVariable Long customerId) {
        transactionDemoService.createOrderAndPaymentThenFail(customerId);
        return "This response should never be reached";
    }

}
