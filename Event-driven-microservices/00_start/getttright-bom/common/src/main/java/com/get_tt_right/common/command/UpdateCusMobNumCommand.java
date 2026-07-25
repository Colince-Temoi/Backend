package com.get_tt_right.common.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

/** As you can see from this command class, we are going to accept customerId, accountNumber, loanNumber, cardNumber, current mobile number and the new mobile number.
 * The TargetAggregateIdentifier is going to be customerId.
 * These fields are very similar to what we have inside the MobileNumberUpdateDto class. The same and equivalent fields we are trying to maintain inside our respective command classes.
 * */
@Builder
@Data
public class UpdateCusMobNumCommand {

    @TargetAggregateIdentifier
    private final String customerId;
    private final Long accountNumber;
    private final Long loanNumber;
    private final Long cardNumber;
    private final String mobileNumber;
    private final String newMobileNumber;

}