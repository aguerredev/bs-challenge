package com.bestseller.starbux.services;

import com.bestseller.starbux.domain.dto.DrinkDTO;
import com.bestseller.starbux.domain.dto.MostUsedToppingForDrinksDTO;

import java.util.List;

public interface OrderDetailService {
    void saveOrderDetail(int orderId, int element, String drink, String topping, int userId);

    List<MostUsedToppingForDrinksDTO> getMostUsedToppingsForDrink(List<DrinkDTO> drinkDTOList);
}
