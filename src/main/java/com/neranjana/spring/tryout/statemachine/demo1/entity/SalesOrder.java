package com.neranjana.spring.tryout.statemachine.demo1.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class SalesOrder {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private OrderState orderState;
    private String description;

}
