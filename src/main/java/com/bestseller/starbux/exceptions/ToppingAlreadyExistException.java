package com.bestseller.starbux.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "That Topping already exist. " +
        "Please, use the correct operation if you want to modify it.")
public class ToppingAlreadyExistException extends Exception {
    public ToppingAlreadyExistException(String name) {
        super("The Topping " + name + " already exists. Please use the correct operation to modify it.");
    }
}
