package com.neranjana.spring.tryout.statemachine.demo1.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class SalesOrder {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private OrderState orderState;
    private String description;
    @Version
    private long version = 0L;

}
