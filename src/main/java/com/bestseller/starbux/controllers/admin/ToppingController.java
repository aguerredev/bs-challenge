package com.bestseller.starbux.controllers.admin;

import com.bestseller.starbux.domain.dto.ToppingDTO;
import com.bestseller.starbux.exceptions.ToppingAlreadyExistException;
import com.bestseller.starbux.exceptions.ToppingNotFoundException;
import com.bestseller.starbux.services.ToppingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * All operations with a Topping by an Admin will be routed by this controller.
 */
@RestController
public class ToppingController extends AdminController{
    private final ToppingService toppingService;

    public ToppingController(final ToppingService toppingService) {
        this.toppingService = toppingService;
    }

    @Operation(summary = "Create a Topping from a toppingDTO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The Topping was successfully created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ToppingDTO.class)) }),
            @ApiResponse(responseCode = "409", description = "Topping already exists.",
                    content = @Content)})
    @PostMapping("/toppings")
    public ToppingDTO create(@RequestBody ToppingDTO drinkDTO) throws ToppingAlreadyExistException {
        return toppingService.create(drinkDTO);
    }

    @Operation(summary = "Update a Topping from a toppingDTO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The Topping was successfully updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ToppingDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "The Topping does not exist.",
                    content = @Content)})
    @PutMapping("/toppings")
    public ToppingDTO update(@RequestBody ToppingDTO drinkDTO) throws ToppingNotFoundException {
        return toppingService.update(drinkDTO);
    }

    @Operation(summary = "Delete a Topping by its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The Topping was successfully deleted",
                    content = { @Content()}),
            @ApiResponse(responseCode = "404", description = "The Topping does not exist.",
                    content = @Content)})
    @DeleteMapping("/toppings/{toppingName}")
    public void delete(@PathVariable String toppingName) throws ToppingNotFoundException {
        toppingService.delete(toppingName);
    }
}
