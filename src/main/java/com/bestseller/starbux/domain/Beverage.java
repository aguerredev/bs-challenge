package com.bestseller.starbux.domain;

import com.bestseller.starbux.domain.dto.DrinkDTO;
import com.bestseller.starbux.domain.dto.ToppingDTO;

import java.util.List;
import java.util.Optional;

public class Beverage {
    private DrinkDTO drink;
    private Optional<List<ToppingDTO>> topping;

    public Beverage(DrinkDTO drink, Optional<List<ToppingDTO>> topping) {
        this.drink = drink;
        this.topping = topping;
    }

    public DrinkDTO getDrink() {
        return drink;
    }

    public Optional<List<ToppingDTO>> getTopping() {
        return topping;
    }
}
