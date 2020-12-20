package com.bestseller.starbux.controllers.admin;

import com.bestseller.starbux.domain.dto.DrinkDTO;
import com.bestseller.starbux.domain.dto.MostUsedToppingForDrinksDTO;
import com.bestseller.starbux.domain.dto.OrdersPerCustomerDTO;
import com.bestseller.starbux.domain.dto.UserDTO;
import com.bestseller.starbux.exceptions.NoDrinksFoundException;
import com.bestseller.starbux.exceptions.NoUsersFoundException;
import com.bestseller.starbux.services.DrinkService;
import com.bestseller.starbux.services.OrderDetailService;
import com.bestseller.starbux.services.OrderService;
import com.bestseller.starbux.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * All operations related to Admin Reports will be routed by this controller.
 */
@RestController
public class ReportController extends AdminController {

    private final UserService userService;
    private final OrderService orderService;
    private final DrinkService drinkService;
    private final OrderDetailService orderDetailService;

    public ReportController(final UserService userService, final OrderService orderService,
                            final DrinkService drinkService, final OrderDetailService orderDetailService) {
        this.userService = userService;
        this.orderService = orderService;
        this.drinkService = drinkService;
        this.orderDetailService = orderDetailService;
    }

    @Operation(summary = "Get the total amount of orders for every customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The Report was successfully created",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "No users were found to create a report.",
                    content = @Content)})
    @GetMapping("/reports/totals")
    public List<OrdersPerCustomerDTO> totalAmountsOfOrdersPerCustomer() throws NoUsersFoundException {
        List<UserDTO> userDTOList = userService.find();
        List<OrdersPerCustomerDTO> ordersPerCustomerDTOList = orderService.getOrdersPerCustomerList(userDTOList);
        return ordersPerCustomerDTOList;
    }

    @Operation(summary = "Get the most used toppings for every drink")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The Report was successfully created",
                    content = { @Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "No drinks were found to create a report.",
                    content = @Content)})
    @GetMapping("/reports/toppings")
    public List<MostUsedToppingForDrinksDTO> mostUsedToppingsForDrink() throws NoDrinksFoundException {
        List<DrinkDTO> drinkDTOList = drinkService.find();
        List<MostUsedToppingForDrinksDTO> mostUsedToppingForDrinksDTOList =
                orderDetailService.getMostUsedToppingsForDrink(drinkDTOList);
        return mostUsedToppingForDrinksDTOList;
    }
}
