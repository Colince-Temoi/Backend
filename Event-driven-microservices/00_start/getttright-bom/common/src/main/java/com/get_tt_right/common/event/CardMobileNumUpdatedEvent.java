package com.get_tt_right.common.event;

import lombok.Data;

/** Here we have the fields accountNumber, loanNumber, cardNumber, mobileNumber, newMobileNumber and customerId. Same as what we have in the respective command class.
 * */
@Data
public class CardMobileNumUpdatedEvent {

    private Long accountNumber;
    private Long loanNumber;
    private Long cardNumber;
    private String mobileNumber;
    private String newMobileNumber;
    private String customerId;

}