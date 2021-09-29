package com.neranjana.spring.tryout.statemachine.demo1.manager;

import com.neranjana.spring.tryout.statemachine.demo1.entity.OrderEvent;
import com.neranjana.spring.tryout.statemachine.demo1.entity.OrderState;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

import java.util.EnumSet;

@Log
@Configuration
@EnableStateMachineFactory
public class SalesOrderStatemachineConfig extends EnumStateMachineConfigurerAdapter<OrderState, OrderEvent> {

    @Autowired
    SalesOrderManager salesOrderManager;

    @Override
    public void configure(StateMachineStateConfigurer<OrderState, OrderEvent> states)
            throws Exception {
        states.withStates()
                .initial(OrderState.SUBMITTED)
                .states(EnumSet.allOf(OrderState.class))
                .state(OrderState.PAID, stateContext -> {salesOrderManager.whenPaidStateEntered(stateContext);},
                        stateContext -> {salesOrderManager.whenPaidStateExited(stateContext);})
                .state(OrderState.FULFILLED)
                .end(OrderState.FULFILLED)
                .end(OrderState.CANCELLED);
    }


    @Override
    public void configure(StateMachineTransitionConfigurer<OrderState, OrderEvent> transitions)
            throws Exception {
        transitions
                .withExternal()
                .source(OrderState.SUBMITTED)
                .target(OrderState.PAID)
                .event(OrderEvent.PAY)
                .and()
                .withExternal()
                .source(OrderState.SUBMITTED)
                .target(OrderState.CANCELLED)
                .event(OrderEvent.CANCEL)
                .and()
                .withExternal()
                .source(OrderState.PAID).guard(salesOrderManager.fulfillGuard())
                .target(OrderState.FULFILLED)
                .event(OrderEvent.FULFILL);
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<OrderState, OrderEvent> config) throws Exception {
        StateMachineListenerAdapter<OrderState, OrderEvent> adapter = new StateMachineListenerAdapter<>() {
            @Override
            public void stateChanged(State<OrderState, OrderEvent> fromState, State<OrderState, OrderEvent> toState) {
                // Do something when the state changes
                log.info("The state changed from " + fromState + " to " + toState);
            }
            @Override
            public void transition(Transition<OrderState, OrderEvent> transition) {
                log.info("Transition " + transition );
            }

            @Override
            public void eventNotAccepted(Message<OrderEvent> message) {
                log.info("Event " + message.getPayload().toString() + " not accepted");

            }


        };



        config.withConfiguration().listener(adapter);
    }



}
