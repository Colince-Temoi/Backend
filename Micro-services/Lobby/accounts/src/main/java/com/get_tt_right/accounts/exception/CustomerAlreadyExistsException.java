package com.get_tt_right.accounts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * This exception is thrown when a customer already exists in the database.
 * Any custom exception should extend RuntimeException.
 * This exception is annotated with @ResponseStatus, which will return a 400 Bad Request status code when this exception is thrown.
 * This class has a constructor that accepts a message, which is passed to the super class.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CustomerAlreadyExistsException extends RuntimeException {
    public CustomerAlreadyExistsException(String message) {
        super(message);
    }

}