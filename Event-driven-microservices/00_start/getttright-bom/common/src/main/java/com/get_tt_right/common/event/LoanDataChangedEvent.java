package com.get_tt_right.common.event;

import lombok.Data;

/** 1st on top of this class, mention @Data lombok annotation. Followed by inside this event class, the very 1st field is String mobileNumber and the 2nd field is Long loanNumber.
 * Using the mobileNumber field we need to identify the record inside the profile MV/table. Once the record is identified, we are going to populate the loanNumber field.
 * We don't want to maintain any active_sw related field because, whenever the loan data is deleted or not present, we are going to maintain a default value which is zero or null value.
 * */

@Data
public class LoanDataChangedEvent {
    private String mobileNumber;
    private Long loanNumber;
}
