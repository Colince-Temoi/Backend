package com.get_tt_right.common.event;

import lombok.Data;

/** 1st on top of this class, mention @Data lombok annotation. Followed by inside this event class, the very 1st field is String name, the 2nd field is String mobileNumber and the 3rd field is boolean activeSw. Reason for these fields: We are looking to store these fields inside the Profile ms.
 * That's why whenever my customer ms is publishing an event, about the data change, it should include the name, mobileNumber and activeSw fields because we are looking to maintain these fields inside the MV DB.
 * So, these are the 3 fields that I require from the customer ms. We don't require email and other fields related to the customer ms. Next, let's go to the AccountsDataChangedEvent, LoansDataChangedEvent and CardsDataChangedEvent classes.
 * */
@Data
public class CustomerDataChangedEvent {
    private String name;
    private String mobileNumber;
    private boolean activeSw;
}
