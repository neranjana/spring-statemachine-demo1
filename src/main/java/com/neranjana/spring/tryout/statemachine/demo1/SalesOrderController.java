package com.neranjana.spring.tryout.statemachine.demo1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SalesOrderController {

    @Autowired
    SalesOrderManager salesOrderManager;

    @PostMapping("/sales-orders")
    public SalesOrder createSalesOrder(@RequestBody String description) {
        return salesOrderManager.createSalesOrder(description);
    }

    @GetMapping("/sales-orders")
    public Iterable<SalesOrder> getAllSalesOrders() {
        return salesOrderManager.findAllSalesOrders();
    }

    @PatchMapping("/sales-orders/{id}")
    public SalesOrder pay(@PathVariable long id) {
        return salesOrderManager.pay(id);
    }


}


