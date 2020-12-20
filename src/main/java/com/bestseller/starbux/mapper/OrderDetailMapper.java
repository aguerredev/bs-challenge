package com.bestseller.starbux.mapper;

import com.bestseller.starbux.domain.dto.OrderDetailDTO;
import com.bestseller.starbux.entities.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderDetailMapper {
    OrderDetailMapper INSTANCE = Mappers.getMapper( OrderDetailMapper.class );

    OrderDetailDTO orderDetailToOrderDetailDTO(OrderDetail order);
}
