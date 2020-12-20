package com.bestseller.starbux.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The Drink does not exist.")
public class DrinkNotFoundException extends Exception {
    public DrinkNotFoundException(String name) {
        super("Could not find a drink called: " + name);
    }
}
