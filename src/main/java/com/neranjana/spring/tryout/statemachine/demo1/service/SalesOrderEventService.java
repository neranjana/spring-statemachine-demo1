package com.neranjana.spring.tryout.statemachine.demo1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SalesOrderEventService {

    @Autowired
    SalesOrderService salesOrderService;

    @Autowired
    SalesOrderStateMachineService salesOrderStateMachineService;
}
