package com.get_tt_right.common.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

/** This time the TargetAggregateIdentifier is going to be accountNumber. We saw in the UpdateCusMobNumCommand class that the TargetAggregateIdentifier is going to be customerId. So, based upon to which ms a given command class belongs to, then accordingly you need to mention/define the TargetAggregateIdentifier.
 * */
@Data
@Builder
public class UpdateAccntMobileNumCommand {

    @TargetAggregateIdentifier
    private final Long accountNumber;
    private final Long loanNumber;
    private final Long cardNumber;
    private final String mobileNumber;
    private final String newMobileNumber;
    private final String customerId;

}