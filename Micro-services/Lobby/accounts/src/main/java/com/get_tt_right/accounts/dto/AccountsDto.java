package com.get_tt_right.accounts.dto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
/**
 * A DTO representing an account.
 * We specify the fields that we want to expose to the client.
 * This is useful for hiding sensitive information or also filtering out some fields. For example, we might not want to expose the password field of a user.
 * Also, customer_id field is not exposed to the client. Why do they want to know the customer_id? They don't need to know the customer_id which is specific to the database.
 */
@Data
public class AccountsDto {
    @NotEmpty(message = "AccountNumber can not be a null or empty")
    @Pattern(regexp="(^$|[0-9]{10})",message = "AccountNumber must be 10 digits")
    private Long accountNumber;
    @NotEmpty(message = "AccountType can not be a null or empty")
    private String accountType;
    @NotEmpty(message = "BranchAddress can not be a null or empty")
    private String branchAddress;
}