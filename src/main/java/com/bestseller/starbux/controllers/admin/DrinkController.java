package com.bestseller.starbux.controllers.admin;

import com.bestseller.starbux.domain.dto.DrinkDTO;
import com.bestseller.starbux.exceptions.DrinkAlreadyExistException;
import com.bestseller.starbux.exceptions.DrinkNotFoundException;
import com.bestseller.starbux.services.DrinkService;
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
 * All operations with a Drink by an Admin will be routed by this controller.
 */
@RestController
public class DrinkController extends AdminController{
    private final DrinkService drinkService;

    public DrinkController(final DrinkService drinkService) {
        this.drinkService = drinkService;
    }

    @Operation(summary = "Create a Drink from a drinkDTO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The Drink was successfully created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DrinkDTO.class)) }),
            @ApiResponse(responseCode = "409", description = "Drink already exists.",
                    content = @Content)})
    @PostMapping("/drinks")
    public DrinkDTO create(@RequestBody DrinkDTO drinkDTO) throws DrinkAlreadyExistException {
        return drinkService.create(drinkDTO);
    }

    @Operation(summary = "Update a Drink from a drinkDTO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The Drink was successfully updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DrinkDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "The Drink does not exist.",
                    content = @Content)})
    @PutMapping("/drinks")
    public DrinkDTO update(@RequestBody DrinkDTO drinkDTO) throws DrinkNotFoundException {
        return drinkService.update(drinkDTO);
    }

    @Operation(summary = "Delete a Drink by its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The Drink was successfully deleted",
                    content = { @Content()}),
            @ApiResponse(responseCode = "404", description = "The Drink does not exist.",
                    content = @Content)})
    @DeleteMapping("/drinks/{drinkName}")
    public void delete(@PathVariable String drinkName) throws DrinkNotFoundException {
        drinkService.delete(drinkName);
    }
}
