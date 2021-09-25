package com.neranjana.spring.tryout.statemachine.demo1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SalesOrderService {

    @Autowired
    SalesOrderRepository salesOrderRepository;

    @Autowired
    SalesOrderStateMachineService salesOrderStateMachineService;

    public SalesOrder createSalesOrder(String description) {
        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setDescription(description);

        StateMachine<OrderState, OrderEvent> stateMachine = salesOrderStateMachineService.getSalesOrderStateMachine();
        stateMachine.start();
        salesOrder.setOrderState(stateMachine.getState().getId());

        salesOrder = salesOrderRepository.save(salesOrder);
        stateMachine.stop();
        return salesOrder;
    }

    public Iterable<SalesOrder> findAllSalesOrders() {
        return salesOrderRepository.findAll();
    }

    public Optional<SalesOrder> findById(long id) {
        return salesOrderRepository.findById(id);
    }

    public SalesOrder update(SalesOrder salesOrder) {
        return salesOrderRepository.save(salesOrder);
    }
}
