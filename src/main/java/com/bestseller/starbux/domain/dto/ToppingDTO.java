package com.bestseller.starbux.domain.dto;

public class ToppingDTO {
    private String name;
    private int price;

    public ToppingDTO(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
