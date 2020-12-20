package com.bestseller.starbux.services;

import com.bestseller.starbux.domain.dto.DrinkDTO;
import com.bestseller.starbux.exceptions.DrinkAlreadyExistException;
import com.bestseller.starbux.exceptions.DrinkNotFoundException;
import com.bestseller.starbux.exceptions.NoDrinksFoundException;

import java.util.List;

public interface DrinkService {
    DrinkDTO getByName(String name) throws DrinkNotFoundException;

    DrinkDTO create(DrinkDTO drinkDTO) throws DrinkAlreadyExistException;

    DrinkDTO update(DrinkDTO drinkDTO) throws DrinkNotFoundException;

    void delete(String drinkName) throws DrinkNotFoundException;

    List<DrinkDTO> find() throws NoDrinksFoundException;
}
