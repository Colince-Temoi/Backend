package com.get_tt_right.accounts.exception;

import com.get_tt_right.accounts.dto.ErrorResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* In this class we have handled  business/user or programmer defined custom exceptions, any RTEs, as well as input validation errors.
* */
@ControllerAdvice // Using this annotation, we are telling to the Spring Boot framework that whenever an exception is thrown to any of my controllers, handle it with the methods defined in this class.
public class GlobalExceptionHandler  extends ResponseEntityExceptionHandler {

    /**
     * Handle {@link MethodArgumentNotValidException} by returning a 400 Bad Request response
     * with all the validation errors.
     *
     * This wmethod is going to get all the Validation exception details as the first parameter. Headers details as the second parameter. Status code as the third parameter. WebRequest as the fourth parameter.
     * @param ex      the exception to be handled
     * @param headers the current HTTP headers
     * @param status  the HTTP status code
     * @param request the current web request
     *
     * In this method we have written a small logic which will process all the validation exceptions and will return the validation errors as part of the response entity.
     * You can enhance this further based upon your business requirements.
     * @return an error response with all the validation errors
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        // Get all validation errors. This will return a list of all the validation errors that occurred during the request.
        List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();
        // Iterate over the list of validation errors and populate the validationErrors map with the field name and the validation message.
        validationErrorList.forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String validationMsg = error.getDefaultMessage();
            validationErrors.put(fieldName, validationMsg);
        });
        // Sending the validation errors as part of ResponseEntity along with the status code bad request.
        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }
/** This method is used to handle all the exceptions that are not handled by any of the other methods.
 * It will handle the RTEs and execute the logic we have written inside this method.
 * If needed, in real-time scenarios we can enhance this method to trigger an email to operations department or to make an entry into one of the tables where you are maintaining all the exception details for reporting purposes.
 * The scenario can be anything based upon your business requirement.
 * How to test this method? Very easy, as an example lets try to introduce a RTE intentionally by commenting out the @AllArgsConstructor annotation in the AccountsController class.
 * With that, we will be  left by only a default constructor in the AccountsController class with which autowiring will not happen and when no auto-wiring happens, the secondary type we expect to be autowired will be null hence we will get a null pointer exception in all our Api operations.
 * This is just one example, there can be many scenarios where we can get a RTE.
 * This method will return a 500 Internal Server Error response with the error message and other details.
 * @param exception the exception to be handled
 * @param webRequest the current web request - We are passing this because inside our error response dto we have a field called apiPath. This we can get from the webRequest object.
 *                   When we pass false to the getDescription method, I will only get the api path.
 *                   If we pass true, then we will get the complete URL information. This has information like what is the IP address of my client, what is the port number, etc.
 *  We are thus populating the errorResponseDTO object with the api path, error message, status code, and the time when the error occurred.
 *  @return an error response with the error message and other details
 * */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception exception,
                                                                  WebRequest webRequest) {
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                                 WebRequest webRequest) {
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle {@link CustomerAlreadyExistsException} by returning a 400 Bad Request response
     * with the error message and other details.
     *
     * @param exception the exception to be handled
     * @param webRequest the current web request - We are passing this because inside our error response dto we have a field called apiPath. This we can get from the webRequest object.
     *                   When we pass false to the getDescription method, I will only get the api path.
     *                   If we pass true, then we will get the complete URL information. This has information like what is the IP address of my client, what is the port number, etc.
     *  We are thus populating the errorResponseDTO object with the api path, error message, status code, and the time when the error occurred.
     * @return an error response with the error message and other details
     */
    @ExceptionHandler(CustomerAlreadyExistsException.class) // This annotation is used to define the exception that this method is going to handle. Like this, we are telling to Spring Boot that whenever an exception with the type CustomerAlreadyExistsException is thrown to any of my controllers, handle it with this method.
    public ResponseEntity<ErrorResponseDto> handleCustomerAlreadyExistsException(CustomerAlreadyExistsException exception,
                                                                                 WebRequest webRequest){
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }

}