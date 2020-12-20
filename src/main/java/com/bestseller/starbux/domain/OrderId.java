package com.bestseller.starbux.domain;

import java.io.Serializable;

public class OrderId implements Serializable {
    private int id;
    private int element;

    public OrderId() {}

    public OrderId(int id, int element) {
        this.id = id;
        this.element = element;
    }

    public int getId() {
        return id;
    }

    public int getElement() {
        return element;
    }
}
