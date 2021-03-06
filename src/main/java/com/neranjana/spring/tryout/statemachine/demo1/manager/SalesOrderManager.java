package com.neranjana.spring.tryout.statemachine.demo1.manager;

import com.neranjana.spring.tryout.statemachine.demo1.service.SalesOrderService;
import com.neranjana.spring.tryout.statemachine.demo1.service.SalesOrderStateMachineService;
import com.neranjana.spring.tryout.statemachine.demo1.entity.OrderEvent;
import com.neranjana.spring.tryout.statemachine.demo1.entity.OrderState;
import com.neranjana.spring.tryout.statemachine.demo1.entity.SalesOrder;
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

    public SalesOrder fulfill(long id) {
        Optional<SalesOrder> salesOrderOptional = salesOrderService.findById(id);
        salesOrderOptional.ifPresent(salesOrder -> {
                StateMachine<OrderState, OrderEvent> stateMachine = salesOrderStateMachineService.getSalesOrderStateMachine(salesOrder);
                stateMachine.sendEvent(OrderEvent.FULFILL);
                salesOrder.setOrderState(stateMachine.getState().getId());
                salesOrderService.update(salesOrder);
            }

        );
        return salesOrderOptional.get();
    }

    public SalesOrder cancel(long id) {
        Optional<SalesOrder> salesOrderOptional = salesOrderService.findById(id);
        salesOrderOptional.ifPresent(salesOrder -> {
                    StateMachine<OrderState, OrderEvent> stateMachine = salesOrderStateMachineService.getSalesOrderStateMachine(salesOrder);
                    stateMachine.sendEvent(OrderEvent.CANCEL);
                    salesOrder.setOrderState(stateMachine.getState().getId());
                    salesOrderService.update(salesOrder);
                }

        );
        return salesOrderOptional.get();
    }

}
