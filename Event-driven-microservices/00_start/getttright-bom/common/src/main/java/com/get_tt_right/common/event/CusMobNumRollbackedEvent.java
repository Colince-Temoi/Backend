package com.get_tt_right.common.event;

import lombok.Data;

/** Here we have the fields customerId, mobileNumber, newMobileNumber and errorMsg. These are very similar to the fields present inside the equivalent command class.
 * */
@Data
public class CusMobNumRollbackedEvent {
    private String customerId;
    private String mobileNumber;
    private String newMobileNumber;
    private String errorMsg;
}