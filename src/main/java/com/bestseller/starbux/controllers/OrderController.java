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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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


    @Operation(summary = "Add a beverage to a user's cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The Beverage was successfully added to the Cart",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CartDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "The User does not exist.",
                    content = { @Content}),
            @ApiResponse(responseCode = "404", description = "The Drink does not exist.",
                    content = { @Content}),
            @ApiResponse(responseCode = "404", description = "The Topping does not exist.",
                    content = @Content)})
    @PostMapping("/carts")
    public CartDTO add(@PathVariable int userId, @RequestBody BeverageDTO beverageDTO) throws UserNotFoundException,
            DrinkNotFoundException, ToppingNotFoundException {
        UserDTO user = getUser(userId);

        DrinkDTO drink = drinkService.getByName(beverageDTO.getDrink());
        Optional<List<ToppingDTO>> toppings = toppingService.getByName(beverageDTO.getToppings());

        return orderService.add(user, drink, toppings);
    }

    @Operation(summary = "Place an order for a user's Cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The Order was successfully placed",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CartDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "The User does not exist.",
                    content = { @Content}),
            @ApiResponse(responseCode = "404", description = "The Cart does not exist.",
                    content = @Content)})
    @PostMapping()
    public OrderDTO place(@PathVariable int userId) throws UserNotFoundException, CartNotFoundException {
        UserDTO user = getUser(userId);
        return orderService.placeOrder(user);
    }

    private UserDTO getUser(int userId) throws UserNotFoundException {
        return userService.getById(userId);
    }
}
