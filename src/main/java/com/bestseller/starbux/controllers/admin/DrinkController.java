package com.bestseller.starbux.controllers.admin;

import com.bestseller.starbux.domain.dto.DrinkDTO;
import com.bestseller.starbux.exceptions.DrinkAlreadyExistException;
import com.bestseller.starbux.exceptions.DrinkNotFoundException;
import com.bestseller.starbux.services.DrinkService;
import com.bestseller.starbux.services.UserService;
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
    private final UserService userService;

    public DrinkController(final DrinkService drinkService,final UserService userService) {
        this.drinkService = drinkService;
        this.userService = userService;
    }

    @PostMapping("/drinks")
    public DrinkDTO create(@RequestBody DrinkDTO drinkDTO)
            throws DrinkAlreadyExistException {
        return drinkService.create(drinkDTO);
    }

    @PutMapping("/drinks")
    public DrinkDTO update(@RequestBody DrinkDTO drinkDTO)
            throws DrinkNotFoundException {
        return drinkService.update(drinkDTO);
    }

    @DeleteMapping("/drinks/{drinkName}")
    public void delete(@PathVariable String drinkName) throws DrinkNotFoundException {
        drinkService.delete(drinkName);
    }
}
