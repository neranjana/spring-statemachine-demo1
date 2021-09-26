package com.neranjana.spring.tryout.statemachine.demo1.invocation;

import com.neranjana.spring.tryout.statemachine.demo1.manager.SalesOrderManager;
import com.neranjana.spring.tryout.statemachine.demo1.entity.SalesOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/sales-orders/{id}/pay")
    public SalesOrder pay(@PathVariable long id) {
        return salesOrderManager.pay(id);
    }

    @PatchMapping("/sales-orders/{id}/fulfill")
    public SalesOrder fulfill(@PathVariable long id) {
        return salesOrderManager.fulfill(id);
    }

    @PatchMapping("/sales-orders/{id}/cancel")
    public SalesOrder cancel(@PathVariable long id) {
        return salesOrderManager.cancel(id);
    }


}


