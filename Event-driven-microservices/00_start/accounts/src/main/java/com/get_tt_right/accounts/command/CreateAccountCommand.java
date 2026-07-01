package com.get_tt_right.accounts.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

/** Here we are going to have fields like accountNumber which I am going to generate inside my business logic.
 * Account Number is going to act as a target aggregate identifier because the account number is never going to change.
 * */
@Builder
@Data
public class CreateAccountCommand {
    @TargetAggregateIdentifier
    private final Long accountNumber;
    private final String mobileNumber; //End user is going to provide.
    private final String accountType; // I am going to assign inside the business logic.
    private final String branchAddress; // I am going to assign inside the business logic.
    private final boolean activeSw; // I am going to assign inside the business logic.
}
