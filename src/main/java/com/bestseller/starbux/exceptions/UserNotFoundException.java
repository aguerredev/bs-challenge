package com.bestseller.starbux.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The User does not exist.")
public class UserNotFoundException extends Exception {
    public UserNotFoundException (int id) {
        super("Could not find a user with id: " + id);
    }
}
