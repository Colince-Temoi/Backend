package com.get_tt_right.customer.command.event;

import lombok.Data;

/** NOUN+VERB(PastTense)+Event
 * Here, we are only mentioning/defining 2 fields i.e., customerId and activeSw
 * */
@Data
public class CustomerDeletedEvent {
    private String customerId;
    private boolean activeSw;
}
