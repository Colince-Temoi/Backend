package com.get_tt_right.accounts.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/** Whatever data that we receive from the client, we need to validate it.
 * That data will 1st be transferred/converted/transformed to the DTO, and then we will validate it.
 * Whenever validation fails due to the validations we are performing here, we will throw a custom message to the client so that s/he will know what went wrong.
 * If you go to the package jakarta.validation.constraints, you will see a lot of annotations that can be used to validate the data. This you can learn and leverage as per your requirement.
 * Ex:
 * @Digits - will make sure that we are enforcing only numeric values.
 * @Future - will make sure that the date is in the future.
 * @Past - will make sure that the date is in the past.
 * @Positive - will make sure that the value is positive.
 * @PositiveOrZero - will make sure that the value is positive or zero.
 * @Negative - will make sure that the value is negative.
 * @NegativeOrZero - will make sure that the value is negative or zero.
 * @NotBlank - will make sure that the value is not null and not empty.
 *
 * etc.
 * Once this is done, we nee to go to the place where we are accepting this data as input. As you may have guessed, it is the Controller layer.
 * In the controller class, we need to mention a class level annotation i.e., @Validated.
 * In this class we also need to mention parameter level annotation i.e., @Valid just before the @RequestBody annotation.
 * This will tell to the Spring Boot framework to validate the data that is coming in the request body as part of the CustomerDto.
 * After doing all that, we need to go to the GlobalExceptionHandler class and write a method to handle the MethodArgumentNotValidException. This will tell spring boot what to do whenever a validation fails.
 * We need to extend the ResponseEntityExceptionHandler class and override the handleMethodArgumentNotValid method.
 * */
@Data
public class CustomerDto {
    @NotEmpty(message = "Name can not be a null or empty") // This annotation is used to validate that the annotated field is not null or empty.
    @Size(min = 5, max = 30, message = "The length of the customer name should be between 5 and 30")  // This annotation is used to validate that the annotated field is between the specified min and max values.
    private String name;
    @NotEmpty(message = "Email address can not be a null or empty")
    @Email(message = "Email address should be a valid value") // This annotation is used to validate that the annotated field is a valid email address.
    private String email;

    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") //
    private String mobileNumber;
    private AccountsDto accountsDto;
}