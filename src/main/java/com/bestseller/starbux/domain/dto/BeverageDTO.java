package com.bestseller.starbux.domain.dto;

import java.util.List;
import java.util.Optional;

public class BeverageDTO {
    private String drink;
    private Optional<List<String>> toppings;

    public BeverageDTO(String drink, Optional<List<String>> toppings) {
        this.drink = drink;
        if(toppings.isPresent()) {
            this.toppings = toppings;
        }
    }

    public String getDrink() {
        return drink;
    }

    public Optional<List<String>> getToppings() {
        return toppings;
    }
}
