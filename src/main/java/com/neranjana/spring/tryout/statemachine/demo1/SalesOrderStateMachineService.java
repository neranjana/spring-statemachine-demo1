package com.neranjana.spring.tryout.statemachine.demo1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

@Service
public class SalesOrderStateMachineService {

    public static final long TEMP_SALES_ORDER_ID = 0;

    @Autowired
    StateMachineFactory<OrderState, OrderEvent> salesOrdertateMachineFactory;

    public StateMachine<OrderState, OrderEvent> getSalesOrderStateMachine() {
        return salesOrdertateMachineFactory.getStateMachine(String.valueOf(TEMP_SALES_ORDER_ID));
    }

    public  StateMachine<OrderState, OrderEvent> getSalesOrderStateMachine(SalesOrder salesOrder) {
        StateMachine<OrderState, OrderEvent> stateMachine = salesOrdertateMachineFactory.getStateMachine(String.valueOf(salesOrder.getId()));
        stateMachine.stop();
        stateMachine.getStateMachineAccessor().doWithAllRegions(access -> access
                .resetStateMachine(new DefaultStateMachineContext<>(salesOrder.getOrderState(), null, null,null)));
        stateMachine.start();
        return stateMachine;
    }

}
