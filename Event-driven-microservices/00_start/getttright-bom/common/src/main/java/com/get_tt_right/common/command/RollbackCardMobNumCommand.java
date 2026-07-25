package com.get_tt_right.common.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

/** Here I have the fields cardNumber, accountNumber, customerId, mobileNumber, newMobileNumber and errorMsg.
 * From this card ms the rollback of account ms is going to be triggered/initiated and that's why we need to pass both the accountNumber and customerId, because in the account ms the customerId is needed to trigger/initiate the compensation txn/ the rollback on the customer ms. That's the reason we are trying to maintain these fields in this command class.
 * */
@Data
@Builder
public class RollbackCardMobNumCommand {

    @TargetAggregateIdentifier
    private final Long cardNumber;
    private final Long accountNumber;
    private final String customerId;
    private final String mobileNumber;
    private final String newMobileNumber;
    private final String errorMsg;

}