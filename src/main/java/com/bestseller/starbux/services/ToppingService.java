package com.bestseller.starbux.services;

import com.bestseller.starbux.domain.dto.ToppingDTO;
import com.bestseller.starbux.exceptions.ToppingAlreadyExistException;
import com.bestseller.starbux.exceptions.ToppingNotFoundException;

import java.util.List;
import java.util.Optional;

public interface ToppingService {
    Optional<List<ToppingDTO>> getByName(Optional<List<String>> toppings) throws ToppingNotFoundException;

    ToppingDTO create(ToppingDTO drinkDTO) throws ToppingAlreadyExistException;

    ToppingDTO update(ToppingDTO drinkDTO) throws ToppingNotFoundException;

    void delete(String toppingName) throws ToppingNotFoundException;
}
