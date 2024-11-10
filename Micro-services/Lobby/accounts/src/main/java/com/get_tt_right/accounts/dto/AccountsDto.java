package com.get_tt_right.accounts.dto;
import lombok.Data;
/**
 * A DTO representing an account.
 * We specify the fields that we want to expose to the client.
 * This is useful for hiding sensitive information or also filtering out some fields. For example, we might not want to expose the password field of a user.
 * Also, customer_id field is not exposed to the client. Why do they want to know the customer_id? They don't need to know the customer_id which is specific to the database.
 */
@Data
public class AccountsDto {
    private Long accountNumber;
    private String accountType;
    private String branchAddress;
}