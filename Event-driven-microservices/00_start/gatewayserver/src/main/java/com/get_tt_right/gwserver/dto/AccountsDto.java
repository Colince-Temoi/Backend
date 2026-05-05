package com.get_tt_right.gwserver.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/** As you can see this class has some fields defined i.e., account number, mobile number, account type, branch address and active switch.
 *Using these fields only I am going to send the data to the client application.
 * On top of these fields I have mentioned some validation related annotations i.e., @NotEmpty, @Pattern. You already know what they do.
 * */
@Data
public class AccountsDto {

    @NotEmpty(message = "AccountNumber can not be a null or empty")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "AccountNumber must be 10 digits")
    private Long accountNumber;

    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    private String mobileNumber;

    @NotEmpty(message = "AccountType can not be a null or empty")
    private String accountType;

    @NotEmpty(message = "BranchAddress can not be a null or empty")
    private String branchAddress;

    private boolean activeSw;

}