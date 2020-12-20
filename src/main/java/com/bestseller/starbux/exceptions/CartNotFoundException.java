package com.bestseller.starbux.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "There's no Cart for that User.")
public class CartNotFoundException extends Exception{
    public CartNotFoundException(int id, String name) {
        super("Could not find a cart for user " + id + "- " + name);
    }
}
