package com.bestseller.starbux.mapper;

import com.bestseller.starbux.domain.dto.OrderDTO;
import com.bestseller.starbux.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper( OrderMapper.class );

    OrderDTO orderToOrderDTO(Order order);
}
