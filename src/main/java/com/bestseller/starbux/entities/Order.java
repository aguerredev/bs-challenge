package com.bestseller.starbux.entities;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "customer_order")
public class Order {
    @Id
    private int id;

    private int userId;

    private int originalAmount;

    private int discountedAmount;

    public Order() {}

    public Order(int id, int userId, int originalAmount, int discountedAmount) {
        this.id = id;
        this.userId = userId;
        this.originalAmount = originalAmount;
        this.discountedAmount = discountedAmount;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getOriginalAmount() {
        return originalAmount;
    }

    public int getDiscountedAmount() {
        return discountedAmount;
    }
}
