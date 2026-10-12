package com.example.springlab.controller;

import com.example.springlab.dto.OrderSummaryDto;
import com.example.springlab.service.OrderQueryDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/lab/orders")
public class OrderQueryDemoController {
    @Autowired
    OrderQueryDemoService orderQueryDemoService;

    @GetMapping("/n-plus-one")
    public List<OrderQueryDemoService.OrderSummary> nPlusOne() {
        return orderQueryDemoService.findAllWithNPlusOne();
    }

    @GetMapping("/fetch-join")
    public List<OrderQueryDemoService.OrderSummary> fetchJoin() {
        return orderQueryDemoService.findAllWithFetchJoin();
    }

    @GetMapping("/dto")
    public List<OrderSummaryDto> dto() {
        return orderQueryDemoService.findAllSummaries();
    }

}
