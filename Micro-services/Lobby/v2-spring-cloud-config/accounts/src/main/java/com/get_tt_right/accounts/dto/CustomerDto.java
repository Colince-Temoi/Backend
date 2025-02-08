package com.get_tt_right.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
/** Inside this CustomerDto, since I don't want to display this technical name in my swagger UI, I can provide a more user-friendly name using the name parameter of @Schema annotation.
 * I can also provide a description about what this schema is going to hold using the description parameter of @Schema annotation.
 * This makes it more business-friendly and professional to the clients.
 * */
@Schema(
        name = "Customer",
        description = "Schema to hold Customer and Account information"
)
@Data
public class CustomerDto {
    /** We are defining field level information as well by using the @Schema annotation.
     * Using name parameter, we can provide a more user-friendly name to the field. But I'm not going to override it because I'm fine with the technical name. Actually it's not even a technical name. It's a business name.
     *  Using description parameter, we can provide a more user-friendly description to the field. Like what the field is going to hold.
     *  Very similarly, I can also invoke the example parameter to provide a sample value for the field. This is very useful for the clients to understand what kind of value they should be passing to this field.
     * */
    @Schema(
            description = "Name of the customer", example = "Colince Temoi"
    )
    @NotEmpty(message = "Name can not be a null or empty") // This annotation is used to validate that the annotated field is not null or empty.
    @Size(min = 5, max = 30, message = "The length of the customer name should be between 5 and 30")  // This annotation is used to validate that the annotated field is between the specified min and max values.
    private String name;

    @Schema(
            description = "Email address of the customer", example = "tutor@get_tt_right.com"
    )
    @NotEmpty(message = "Email address can not be a null or empty")
    @Email(message = "Email address should be a valid value") // This annotation is used to validate that the annotated field is a valid email address.
    private String email;

    @Schema(
            description = "Mobile Number of the customer", example = "9345432123"
    )
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") //
    private String mobileNumber;

    /** Here I will not provide example values because I will do this in the AccountsDto class.
    * */
    @Schema(
            description = "Account details of the Customer"
    )
    private AccountsDto accountsDto;
}