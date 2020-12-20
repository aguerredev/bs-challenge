package com.bestseller.starbux.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The Topping does not exist.")
public class ToppingNotFoundException extends Exception {
    public ToppingNotFoundException(String name) {
        super("Could not find a topping called: " + name);
    }
}
