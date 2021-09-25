package com.neranjana.spring.tryout.statemachine.demo1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SalesOrderManager {

    @Autowired
    SalesOrderService salesOrderService;

    @Autowired
    SalesOrderStateMachineService salesOrderStateMachineService;

    public SalesOrder createSalesOrder(String description) {
        return salesOrderService.createSalesOrder(description);
    }

    public Iterable<SalesOrder> findAllSalesOrders() {
        return salesOrderService.findAllSalesOrders();
    }

    public SalesOrder pay(long id) {
        Optional<SalesOrder> salesOrderOptional = salesOrderService.findById(id);
        salesOrderOptional.ifPresent(salesOrder -> {
                StateMachine<OrderState, OrderEvent> stateMachine = salesOrderStateMachineService.getSalesOrderStateMachine(salesOrder);
                stateMachine.sendEvent(OrderEvent.PAY);
                salesOrder.setOrderState(stateMachine.getState().getId());
                salesOrderService.update(salesOrder);
            }

        );
        return salesOrderOptional.get();
    }
}
