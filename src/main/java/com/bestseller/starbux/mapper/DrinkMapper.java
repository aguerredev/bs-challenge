package com.bestseller.starbux.mapper;

import com.bestseller.starbux.domain.dto.DrinkDTO;
import com.bestseller.starbux.entities.Drink;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DrinkMapper {
    DrinkMapper INSTANCE = Mappers.getMapper( DrinkMapper.class );

    DrinkDTO drinkToDrinkDTO(Drink drink);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "price", target = "price")
    Drink drinkDTOToDrink(DrinkDTO drink);
}
