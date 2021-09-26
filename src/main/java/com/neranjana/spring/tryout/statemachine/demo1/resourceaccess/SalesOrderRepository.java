package com.neranjana.spring.tryout.statemachine.demo1.resourceaccess;

import com.neranjana.spring.tryout.statemachine.demo1.entity.SalesOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalesOrderRepository extends CrudRepository<SalesOrder, Long> {
    Optional<SalesOrder> findById(Long id);

}
