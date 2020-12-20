package com.bestseller.starbux.entities;

import com.bestseller.starbux.domain.OrderId;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "customer_order_detail")
@IdClass(OrderId.class)
public class OrderDetail {
    @Id
    private int id;

    @Id
    private int element;

    private String drink;

    private String topping;

    private int userId;

    public OrderDetail() {}

    public OrderDetail(int id, int element, String drink, String topping, int userId) {
        this.id = id;
        this.element = element;
        this.drink = drink;
        this.topping = topping;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public int getElement() {
        return element;
    }

    public String getDrink() {
        return drink;
    }

    public String getTopping() {
        return topping;
    }

    public int getUserId() {
        return userId;
    }

}
