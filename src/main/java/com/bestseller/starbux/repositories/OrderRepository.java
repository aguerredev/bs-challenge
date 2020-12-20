package com.bestseller.starbux.repositories;

import com.bestseller.starbux.entities.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Database Access Object for customer_order table.
 */

public interface OrderRepository extends CrudRepository<Order, Integer> {
    List<Order> findByUserId(int userId);
}
