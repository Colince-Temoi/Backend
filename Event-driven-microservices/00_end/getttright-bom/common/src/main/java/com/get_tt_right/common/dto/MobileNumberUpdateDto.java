package com.get_tt_right.common.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
/** Make sure to add the dependency related to validation i.e., "spring-boot-starter-validation" inside the pom.xml of the common project to resolve the CE's. Just copy it from one of the projects that has it and paste.
 * */
@Data
public class MobileNumberUpdateDto {

    @NotEmpty(message = "Current mobile number cannot be empty")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    private String currentMobileNumber; // Anyone who wants to invoke my update mobile number API, 1st they have to mention the current mobile number that is being used in all the ms's.

    @NotEmpty(message = "New mobile number cannot be empty")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    private String newMobileNumber; // Followed by the new mobile number that will be used in all the ms's


}