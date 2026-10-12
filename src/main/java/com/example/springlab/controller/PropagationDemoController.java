
package com.example.springlab.controller;

import com.example.springlab.service.PropagationDemoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lab/transactions")
public class PropagationDemoController {

    private final PropagationDemoService demoService;

    public PropagationDemoController(
            PropagationDemoService demoService) {
        this.demoService = demoService;
    }

    @PostMapping("/propagation/{orderId}")
    public String demonstrate(@PathVariable Long orderId) {
        demoService.updateOrderAndFail(orderId);
        return "This response should never be reached";
    }
}
