package com.bestseller.starbux.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "There are no Users.")
public class NoUsersFoundException extends Exception {
    public NoUsersFoundException () {
        super("There are no Users.");
    }
}
