package com.bestseller.starbux.domain.dto;

public class OrderDetailDTO {
    private String drink;
    private String topping;

    public OrderDetailDTO(String drink, String topping) {
        this.drink = drink;
        this.topping = topping;
    }

    public String getDrink() {
        return drink;
    }

    public String getTopping() {
        return topping;
    }
}
