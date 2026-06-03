package com.get_tt_right.customer.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 * The docstring in CreateCustomerCommand also applies here. Reason - During the updation operation we are going to expect the same fields from the end user.
 * */
@Data
@Builder
public class UpdateCustomerCommand {
    @TargetAggregateIdentifier
    private final String customerId;
    private final String name;
    private final String email;
    private final String mobileNumber;
    private final boolean activeSw;
}
