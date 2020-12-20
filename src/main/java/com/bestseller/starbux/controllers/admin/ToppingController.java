package com.bestseller.starbux.controllers.admin;

import com.bestseller.starbux.domain.dto.ToppingDTO;
import com.bestseller.starbux.exceptions.ToppingAlreadyExistException;
import com.bestseller.starbux.exceptions.ToppingNotFoundException;
import com.bestseller.starbux.services.ToppingService;
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

    @PostMapping("/toppings")
    public ToppingDTO create(@RequestBody ToppingDTO drinkDTO) throws ToppingAlreadyExistException {
        return toppingService.create(drinkDTO);
    }

    @PutMapping("/toppings")
    public ToppingDTO update(@RequestBody ToppingDTO drinkDTO) throws ToppingNotFoundException {
        return toppingService.update(drinkDTO);
    }

    @DeleteMapping("/toppings/{toppingName}")
    public void delete(@PathVariable String toppingName) throws ToppingNotFoundException {
        toppingService.delete(toppingName);
    }
}
