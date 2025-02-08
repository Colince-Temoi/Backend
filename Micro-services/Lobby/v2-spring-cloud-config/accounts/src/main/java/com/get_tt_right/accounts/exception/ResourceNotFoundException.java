package com.get_tt_right.accounts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception is thrown when a resource is not found in the database.
 * Any custom exception should extend RuntimeException.
 * This exception is annotated with @ResponseStatus, which will return a 404 Not Found status code when this exception is thrown.
 * This class has a constructor that accepts the resource name, field name, and field value, which is passed to the super class.
 * Like this we are passing more than one parameter to this constructor so that we can send a detailed exception message to the client.
 * The problem we have is, this RuntimeException constructor is going to accept only one parameter - A single String.
 * The solution would be to prepare a single string based upon the 3 parameters and pass it to the super class.
 * For the same we are making use of String.format() method.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s not found with the given input data %s : '%s'", resourceName, fieldName, fieldValue));
    }

}