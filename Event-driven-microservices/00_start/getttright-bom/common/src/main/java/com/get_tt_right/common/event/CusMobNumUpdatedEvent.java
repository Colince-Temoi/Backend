package com.get_tt_right.common.event;

import lombok.Data;

/** Here we have the fields customerId, accountNumber, loanNumber, cardNumber, mobileNumber and newMobileNumber. Same as what we have in the respective command class.
 * */
@Data
public class CusMobNumUpdatedEvent {
    private String customerId;
    private Long accountNumber;
    private Long loanNumber;
    private Long cardNumber;
    private String mobileNumber;
    private String newMobileNumber;
}