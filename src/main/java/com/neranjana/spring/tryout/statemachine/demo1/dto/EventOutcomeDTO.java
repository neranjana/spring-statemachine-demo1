package com.neranjana.spring.tryout.statemachine.demo1.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EventOutcomeDTO<T, S, U> {

    private T entity;
    private S initialState;
    private S endState;
    private U event;
    private boolean stateChanged;
    private List<String> errorMessages = new ArrayList<>();


}
