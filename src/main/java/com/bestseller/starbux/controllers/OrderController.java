package com.bestseller.starbux.controllers;

import com.bestseller.starbux.domain.dto.CartDTO;
import com.bestseller.starbux.domain.dto.BeverageDTO;
import com.bestseller.starbux.domain.dto.DrinkDTO;
import com.bestseller.starbux.domain.dto.OrderDTO;
import com.bestseller.starbux.domain.dto.ToppingDTO;
import com.bestseller.starbux.domain.dto.UserDTO;
import com.bestseller.starbux.exceptions.CartNotFoundException;
import com.bestseller.starbux.exceptions.DrinkNotFoundException;
import com.bestseller.starbux.exceptions.ToppingNotFoundException;
import com.bestseller.starbux.exceptions.UserNotFoundException;
import com.bestseller.starbux.services.DrinkService;
import com.bestseller.starbux.services.OrderService;
import com.bestseller.starbux.services.ToppingService;
import com.bestseller.starbux.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * All operations with an order will be routed by this controller.
 */
@RestController
@RequestMapping("v1/orders/{userId}")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final DrinkService drinkService;
    private final ToppingService toppingService;

    @Autowired
    public OrderController(final OrderService orderService, final UserService userService,
                           final DrinkService drinkService, final ToppingService toppingService) {
        this.orderService = orderService;
        this.userService = userService;
        this.drinkService = drinkService;
        this.toppingService = toppingService;
    }

    @PostMapping("/carts")
    public CartDTO add(@PathVariable int userId, @RequestBody BeverageDTO beverageDTO) throws UserNotFoundException,
            DrinkNotFoundException, ToppingNotFoundException {
        UserDTO user = getUser(userId);

        DrinkDTO drink = drinkService.getByName(beverageDTO.getDrink());
        Optional<List<ToppingDTO>> toppings = toppingService.getByName(beverageDTO.getToppings());

        return orderService.add(user, drink, toppings);
    }

    @PostMapping()
    public OrderDTO place(@PathVariable int userId) throws UserNotFoundException, CartNotFoundException {
        UserDTO user = getUser(userId);
        return orderService.placeOrder(user);
    }

    private UserDTO getUser(int userId) throws UserNotFoundException {
        return userService.getById(userId);
    }
}
