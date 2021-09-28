package com.neranjana.spring.tryout.statemachine.demo1.service;

import com.neranjana.spring.tryout.statemachine.demo1.entity.OrderEvent;
import com.neranjana.spring.tryout.statemachine.demo1.entity.OrderState;
import com.neranjana.spring.tryout.statemachine.demo1.entity.SalesOrder;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Service;

@Log
@Service
public class SalesOrderStateMachineService {

    public static final long TEMP_SALES_ORDER_ID = 0;
    public static final String SALES_ORDER_STATE_MACHINE_VARIABLE = "SALES_ORDER";

    @Autowired
    SalesOrderService salesOrderService;
    @Autowired
    StateMachineFactory<OrderState, OrderEvent> salesOrdertateMachineFactory; // The IntelliJ IDE will highlight this could not autowire. But it is fine.

    public StateMachine<OrderState, OrderEvent> getSalesOrderStateMachine() {
        return salesOrdertateMachineFactory.getStateMachine(String.valueOf(TEMP_SALES_ORDER_ID));
    }

    public StateMachine<OrderState, OrderEvent> getSalesOrderStateMachine(SalesOrder salesOrder) {
        StateMachine<OrderState, OrderEvent> stateMachine = salesOrdertateMachineFactory.getStateMachine(String.valueOf(salesOrder.getId()));
        stateMachine.stop();
        stateMachine.getStateMachineAccessor().doWithAllRegions(stateMachineAccessor -> {
            stateMachineAccessor.addStateMachineInterceptor(new StateMachineInterceptorAdapter<OrderState, OrderEvent>() {
                @Override
                public void preStateChange(State<OrderState, OrderEvent> state, Message<OrderEvent> eventMessage, Transition<OrderState, OrderEvent> transition,
                    StateMachine<OrderState, OrderEvent> statMachine) {
                    log.info("State is about to change");

                }

                public void postStateChange(State<OrderState, OrderEvent> state, Message<OrderEvent> eventMessage, Transition<OrderState, OrderEvent> transition,
                                           StateMachine<OrderState, OrderEvent> statMachine) {
                    SalesOrder salesOrder = (SalesOrder) statMachine.getExtendedState().getVariables().get(SALES_ORDER_STATE_MACHINE_VARIABLE);
                    salesOrder.setOrderState(stateMachine.getState().getId());
                    salesOrderService.update(salesOrder);

                }
            });
            stateMachineAccessor.resetStateMachine(new DefaultStateMachineContext<>(salesOrder.getOrderState(), null, null, null));
        });
        stateMachine.start();
        return stateMachine;
    }

}
