package com.bestseller.starbux.domain.dto;

import java.util.List;

public class MostUsedToppingForDrinksDTO {
    private String drink;
    private List<String> mostUsedToppings;

    public MostUsedToppingForDrinksDTO(String drink, List<String> mostUsedToppings) {
        this.drink = drink;
        this.mostUsedToppings = mostUsedToppings;
    }

    public String getDrink() {
        return drink;
    }

    public List<String> getMostUsedToppings() {
        return mostUsedToppings;
    }
}
