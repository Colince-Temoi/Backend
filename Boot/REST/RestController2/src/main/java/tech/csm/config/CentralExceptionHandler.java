package tech.csm.config;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.Getter;
import lombok.Setter;
import tech.csm.exception.ApiErrorResponse;

/*
 * This is an example of a central exception handler class in tech.csm.exception package, which will act as an interceptor for all exceptions thrown from controllers across the application
 * The '@ControllerAdvice' annotation tells Spring that this class contains exception-handling logic for web controllers. The '@ExceptionHandler' methods within this class specify which exceptions to handle and how to handle them.
 * For this specific case, the 'handleArithmeticException' method handles 'ArithmeticException', which will be triggered when an integer division by zero occurs. The general 'handleGeneralException' method will handle any other exceptions that are not specifically caught.
 * Additional Customizations:You can further customize the response to your needs. For example, you can create an 'ApiErrorResponse' class to hold detailed error information.
 * With this setup, any unhandled exceptions from your controllers will be intercepted by the CentralExceptionHandler, and a standardized error response will be sent back to the client, hiding implementation-specific details of the exception.
 * 
 * */

@ControllerAdvice
public class CentralExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex) {
        // Log the exception here if needed
        // log.error("Unhandled exception:", ex);

        // Customize your response body or use the exception message
        String message = "Something went wrong. Please try again later.";

        // You can also add more details about the error to the response body
        ApiErrorResponse errorResponse = new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, message, ex.getMessage(), message);

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Special handler for ArithmeticException (divide by zero)
    @ExceptionHandler(ArithmeticException.class)
    public ResponseEntity<Object> handleArithmeticException(ArithmeticException ex) {
        String message = "Invalid operation: Division by zero is not allowed.";
        ApiErrorResponse errorResponse = new ApiErrorResponse(HttpStatus.BAD_REQUEST, message, ex.getMessage(), message);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    /*
     * Here's the refactored code that moves the validation error handling to a separate @ExceptionHandler method. This follows the better practice of keeping controller methods clean and focused on handling the business logic, while delegating error handling to separate concerns
     * In this improved version, we've transferred the error handling logic to the GlobalExceptionHandler class's handleValidationErrors method. This method is now specifically designed to handle MethodArgumentNotValidException exceptions, thereby adhering to the single responsibility principle.

The ErrorResponse class is a new addition, serving as a data structure to encapsulate the error response body. It provides a more organized way to send the error messages back to the client.

With this refactoring, your controller method remains clean and focused, while the exception handling has been elegantly tucked away in the @ExceptionHandler method.
If you want to customize the error response, you can add a @ExceptionHandler(MethodArgumentNotValidException.class) method to your controller
     * This will return a 400 Bad Request response with a list of error messages in the response body.
     * 
     * */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorMessages(errors);
        return ResponseEntity.badRequest().body(errorResponse);
    }
    
    @Setter @Getter
    class ErrorResponse{
        private List<String> errorMessages;
    }
    
}