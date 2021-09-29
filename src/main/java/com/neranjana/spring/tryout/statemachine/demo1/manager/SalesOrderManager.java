package com.neranjana.spring.tryout.statemachine.demo1.manager;

import com.neranjana.spring.tryout.statemachine.demo1.dto.EventOutcomeDTO;
import com.neranjana.spring.tryout.statemachine.demo1.service.SalesOrderService;
import com.neranjana.spring.tryout.statemachine.demo1.service.SalesOrderStateMachineService;
import com.neranjana.spring.tryout.statemachine.demo1.entity.OrderEvent;
import com.neranjana.spring.tryout.statemachine.demo1.entity.OrderState;
import com.neranjana.spring.tryout.statemachine.demo1.entity.SalesOrder;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log
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

    public EventOutcomeDTO<SalesOrder, OrderState, OrderEvent> handleEvent(long id, OrderEvent event) {
        Optional<SalesOrder> salesOrderOptional = salesOrderService.findById(id);

        EventOutcomeDTO<SalesOrder, OrderState, OrderEvent> eventOutcomeDTO = salesOrderStateMachineService.handleEvent(salesOrderOptional.get(), event);

        return eventOutcomeDTO;
    }

    public void whenPaidStateEntered(StateContext<OrderState, OrderEvent> stateContext) {
        SalesOrder salesOrder = (SalesOrder) stateContext.getExtendedState().getVariables().get(SalesOrderStateMachineService.SALES_ORDER_STATE_MACHINE_VARIABLE);
        log.info("SalesOrder " + salesOrder.getId() + " entered PAID state");

    }

    public void whenPaidStateExited(StateContext<OrderState, OrderEvent> stateContext) {
        SalesOrder salesOrder = (SalesOrder) stateContext.getExtendedState().getVariables().get(SalesOrderStateMachineService.SALES_ORDER_STATE_MACHINE_VARIABLE);
        log.info("SalesOrder " + salesOrder.getId() + " exited PAID state");
    }

    @Bean
    public Guard<OrderState, OrderEvent> fulfillGuard() {
        return new Guard<OrderState, OrderEvent>() {

            @Override
            public boolean evaluate(StateContext<OrderState, OrderEvent> stateContext) {
                SalesOrder salesOrder = (SalesOrder) stateContext.getExtendedState().getVariables().get(SalesOrderStateMachineService.SALES_ORDER_STATE_MACHINE_VARIABLE);
                EventOutcomeDTO<SalesOrder, OrderState, OrderEvent> eventOutcomeDTO = (EventOutcomeDTO<SalesOrder, OrderState, OrderEvent>) stateContext.getExtendedState().
                        getVariables().get(SalesOrderStateMachineService.EVENT_OUTCOME_STATE_MACHINE_VARIABLE);
                if ("Pen".equals(salesOrder.getDescription()) || "Pencil".equals(salesOrder.getDescription())) {
                    return true;
                } else {
                    eventOutcomeDTO.getErrorMessages().add("Could not fulfill because the description is not a Pen or a Pencil.");
                    return false;
                }
            }
        };
    }
}
