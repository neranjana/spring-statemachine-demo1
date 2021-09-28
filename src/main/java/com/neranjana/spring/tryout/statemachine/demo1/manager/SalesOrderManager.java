package com.neranjana.spring.tryout.statemachine.demo1.manager;

import com.neranjana.spring.tryout.statemachine.demo1.service.SalesOrderService;
import com.neranjana.spring.tryout.statemachine.demo1.service.SalesOrderStateMachineService;
import com.neranjana.spring.tryout.statemachine.demo1.entity.OrderEvent;
import com.neranjana.spring.tryout.statemachine.demo1.entity.OrderState;
import com.neranjana.spring.tryout.statemachine.demo1.entity.SalesOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
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

    public SalesOrder handleEvent(long id, OrderEvent event) {
        Optional<SalesOrder> salesOrderOptional = salesOrderService.findById(id);
        salesOrderOptional.ifPresent(salesOrder -> {
                    StateMachine<OrderState, OrderEvent> stateMachine = salesOrderStateMachineService.getSalesOrderStateMachine(salesOrder);
                    stateMachine.getExtendedState().getVariables().putIfAbsent(SalesOrderStateMachineService.SALES_ORDER_STATE_MACHINE_VARIABLE, salesOrder);
                    Message<OrderEvent> eventMessage = MessageBuilder.withPayload(event).setHeader("order-id", id).build();
                    stateMachine.sendEvent(eventMessage);
                    salesOrder.setOrderState(stateMachine.getState().getId());
                }

        );
        return salesOrderOptional.get();
    }








}
