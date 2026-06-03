package com.get_tt_right.customer.command.event;

import lombok.Data;

/** In the case of event classes scenario, you need to follow a different naming convention which is - NOUN + VERB(in past tense) + Event suffix. For example - CustomerCreatedEvent, CustomerUpdatedEvent, CustomerDeletedEvent, etc. Reason: By the time the command completes executing its logic, the customer will have already been written in the write DB that's why the event naming convention should convey the same i.e., Customer Created, Customer Updated, Customer Deleted, etc. By following this pattern only we need to create all these event classes.
 * Inside this class we are going to create multiple fields. We didn't mark any of these fields as final, because in the case of events - sometimes once the object of event is created and before we try to publish it into event bus, we may have some requirements to change some data and that's why I don't want to mark these fields as final.
 * Next, on top of this class I am mentioning the @Data annotation.
 * Inside this class, we don't have to mention any of the annotations like @TartgetAggregateIdentifier because event classes are only data carriers. Because, for example, once the command is completed, the event object will be published into the event bus and on the read side, this event will be read and the data will be processed.
 * Since there is no storage or DB involved, there is no need to mention any of the annotations like @TargetAggregateIdentifier.
 * */
@Data
public class CustomerCreatedEvent {
    private String customerId;
    private String name;
    private String email;
    private String mobileNumber;
    private boolean activeSw;
}
