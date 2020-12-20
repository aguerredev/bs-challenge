package com.bestseller.starbux.services;

import com.bestseller.starbux.domain.dto.CartDTO;
import com.bestseller.starbux.domain.dto.DrinkDTO;
import com.bestseller.starbux.domain.dto.OrderDTO;
import com.bestseller.starbux.domain.dto.OrdersPerCustomerDTO;
import com.bestseller.starbux.domain.dto.ToppingDTO;
import com.bestseller.starbux.domain.dto.UserDTO;
import com.bestseller.starbux.exceptions.CartNotFoundException;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    CartDTO add(UserDTO user, DrinkDTO drink, Optional<List<ToppingDTO>> toppings);

    OrderDTO placeOrder(UserDTO user) throws CartNotFoundException;

    List<OrdersPerCustomerDTO> getOrdersPerCustomerList(List<UserDTO> userDTOList);
}
