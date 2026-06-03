package com.get_tt_right.customer.command.event;

import lombok.Data;

/** NOUN+VERB(PastTense)+Event
 * The same fields present inside the CustomerCreatedEvent we have them inside the CustomerUpdatedEvent class.
 * */
@Data
public class CustomerUpdatedEvent {
    private String customerId;
    private String name;
    private String email;
    private String mobileNumber;
    private boolean activeSw;
}
