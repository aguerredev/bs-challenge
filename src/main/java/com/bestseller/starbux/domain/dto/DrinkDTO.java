package com.bestseller.starbux.domain.dto;

public class DrinkDTO {
    private String name;
    private int price;

    public DrinkDTO(String name, int price) {
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
