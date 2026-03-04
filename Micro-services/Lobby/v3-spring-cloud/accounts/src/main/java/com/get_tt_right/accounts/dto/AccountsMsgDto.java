package com.get_tt_right.accounts.dto;

/**
 * Here we are storing what is the Account Number, name, email and the mobile number of the customer.
 * */
public record AccountsMsgDto(Long accountNumber, String name, String email, String mobileNumber) {
}
