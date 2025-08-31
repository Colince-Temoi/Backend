package com.get_tt_right.message.dto;

/**
 * Here we are receiving what is the Account Number, name, email and the mobile number of the customer. Using this information, my message ms can send communication in the form of email and sms to the customer.
 * */
public record AccountsMsgDto(Long accountNumber, String name, String email, String mobileNumber) {
}
