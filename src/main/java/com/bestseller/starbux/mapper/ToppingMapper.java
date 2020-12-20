package com.bestseller.starbux.mapper;

import com.bestseller.starbux.domain.dto.ToppingDTO;
import com.bestseller.starbux.entities.Topping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ToppingMapper {
    ToppingMapper INSTANCE = Mappers.getMapper( ToppingMapper.class );

    ToppingDTO toppingToToppingDTO(Topping topping);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "price", target = "price")
    Topping toppingDTOToTopping(ToppingDTO topping);
}
