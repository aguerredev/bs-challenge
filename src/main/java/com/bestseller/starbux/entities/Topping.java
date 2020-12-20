package com.bestseller.starbux.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Topping {
    @Id
    private String name;
    private int price;

    public  Topping() {}

    public Topping(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
