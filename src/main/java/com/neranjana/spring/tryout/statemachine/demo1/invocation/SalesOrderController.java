package com.neranjana.spring.tryout.statemachine.demo1.invocation;

import com.neranjana.spring.tryout.statemachine.demo1.entity.OrderEvent;
import com.neranjana.spring.tryout.statemachine.demo1.entity.OrderState;
import com.neranjana.spring.tryout.statemachine.demo1.manager.SalesOrderManager;
import com.neranjana.spring.tryout.statemachine.demo1.entity.SalesOrder;
import com.neranjana.spring.tryout.statemachine.demo1.dto.EventOutcomeDTO;
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


    @PatchMapping("/sales-orders/{id}/events/{event}")
    public EventOutcomeDTO<SalesOrder, OrderState, OrderEvent> handleEvent(@PathVariable long id, @PathVariable OrderEvent event) {
        return salesOrderManager.handleEvent(id, event);
    }


}


