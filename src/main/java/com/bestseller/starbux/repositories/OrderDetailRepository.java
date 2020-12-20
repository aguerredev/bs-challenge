package com.bestseller.starbux.repositories;

import com.bestseller.starbux.domain.OrderId;
import com.bestseller.starbux.entities.OrderDetail;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Database Access Object for customer_order_detail table.
 */

public interface OrderDetailRepository extends CrudRepository<OrderDetail, OrderId> {
    List<OrderDetail> findByDrink(String drink);
}
