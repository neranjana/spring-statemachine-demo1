package com.neranjana.spring.tryout.statemachine.demo1;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Data
public class SalesOrder {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private OrderState orderState;
    private String description;

}
