package com.get_tt_right.gwserver.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/** As you can see this class has some fields defined i.e., name, email, mobile number and active switch.
 *Using these fields only I am going to send the data to the client application.
 * On top of these fields I have mentioned some validation related annotations i.e., @NotEmpty, @Size, @Email and @Pattern. You already know what they do.
 * */
@Data
public class CustomerDto {

    @NotEmpty(message = "Name can not be a null or empty")
    @Size(min = 5, max = 30, message = "The length of the accounts name should be between 5 and 30")
    private String name;

    @NotEmpty(message = "Email address can not be a null or empty")
    @Email(message = "Email address should be a valid value")
    private String email;

    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    private String mobileNumber;

    private boolean activeSw;
}