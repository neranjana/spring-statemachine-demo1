package com.neranjana.spring.tryout.statemachine.demo1.service;

import com.neranjana.spring.tryout.statemachine.demo1.resourceaccess.SalesOrderRepository;
import com.neranjana.spring.tryout.statemachine.demo1.entity.OrderEvent;
import com.neranjana.spring.tryout.statemachine.demo1.entity.OrderState;
import com.neranjana.spring.tryout.statemachine.demo1.entity.SalesOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class SalesOrderService {

    @Autowired
    SalesOrderRepository salesOrderRepository;

    @Autowired
    SalesOrderStateMachineService salesOrderStateMachineService;

    public SalesOrder createSalesOrder(String description) {
        SalesOrder salesOrder = new SalesOrder();
        Optional<SalesOrder> updatedSalesOrder;
        salesOrder.setDescription(description);


        StateMachine<OrderState, OrderEvent> stateMachine = salesOrderStateMachineService.getSalesOrderStateMachine();
        stateMachine.start();
        salesOrder.setOrderState(stateMachine.getState().getId());

        salesOrder = salesOrderRepository.save(salesOrder);
        updatedSalesOrder = salesOrderRepository.findById(salesOrder.getId());
        stateMachine.stop();
        return updatedSalesOrder.get();
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
