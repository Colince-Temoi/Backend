package com.get_tt_right.common.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/** Here we are trying to accept various fields as part of the update mobile number request. Using currentMobileNumber we want to accept the existing mobile number whereas the newMobileNumber is the value which we want to be updated inside all the 4 ms's.
 * Apart from these mobile number related fields I am also trying to accept the customerId, accountNumber, loanNumber and cardNumber. There is a reason as to why I am trying to accept these fields. If you open any of the aggregate class i.e., CustomerAggregate Class, whenever we want to save the data inside the write DB by using ES, it is going to accept the customerId which is the aggregate identifier. Similarly, inside the loans, cards and accounts ms's the aggregate identifier is going to be accountNumber, loanNumber and cardNumber in the respective aggregate class. These aggregate classes expect the aggregate identifiers in order to make the updates inside the ES DB. So, to accommodate the changes of the CQRS + ES pattern I am trying to accept these 4 fields from the client application itself.
 * Anyway the client application can trigger the API - fetchCustomerSummary or fetchProfile inside the gatewayserver or profile ms respectively which is going to give the details of the customer, loan, card and account and from that, the client application should be able to know that this customerId, accountNumber, loanNumber and cardNumber. If client application is not able to send these details, that is also fine - alternatively we can run a query inside the command interceptor classes to lead these details based upon the currentMobileNumber. Since we are looking to avoid unnecessary DB queries, we are trying to accept these details from the client application itself. Hope you are crisp clear.
 * Note: @NotEmpty validation constraint will only work for the String fields and not Long fields.
 * */
@Data
public class MobileNumberUpdateDto {

    @NotEmpty(message = "Customer ID cannot be empty")
    private String customerId;

    private Long accountNumber;

    private Long loanNumber;

    private Long cardNumber;

    @NotEmpty(message = "Current mobile number cannot be empty")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    private String currentMobileNumber;

    @NotEmpty(message = "New mobile number cannot be empty")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    private String newMobileNumber;

}