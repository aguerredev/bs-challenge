package com.bestseller.starbux.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "There are no Drinks.")
public class NoDrinksFoundException extends Exception {
    public NoDrinksFoundException() {
        super("There are no Drinks.");
    }
}
