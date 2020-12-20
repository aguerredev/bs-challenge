package com.bestseller.starbux.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "That Drink already exist. " +
        "Please, use the correct operation if you want to modify it.")
public class DrinkAlreadyExistException extends Exception {
    public DrinkAlreadyExistException(String name) {
        super("The Drink " + name + " already exists. Please use the correct operation to modify it.");
    }
}
