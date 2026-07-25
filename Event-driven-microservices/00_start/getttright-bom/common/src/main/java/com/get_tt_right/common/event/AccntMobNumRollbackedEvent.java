package com.get_tt_right.common.event;

import lombok.Data;

/** Here we have the fields customerId, accountNumber, mobileNumber, newMobileNumber and errorMsg. These are very similar to the fields present inside the equivalent command class.
 * */
@Data
public class AccntMobNumRollbackedEvent {
    private String customerId;
    private Long accountNumber;
    private String mobileNumber;
    private String newMobileNumber;
    private String errorMsg;
}