package com.get_tt_right.customer.query.projection;

import com.get_tt_right.customer.command.event.CustomerCreatedEvent;
import com.get_tt_right.customer.command.event.CustomerDeletedEvent;
import com.get_tt_right.customer.command.event.CustomerUpdatedEvent;
import com.get_tt_right.customer.entity.Customer;
import com.get_tt_right.customer.service.ICustomerService;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/** First, on top of this class we need to mention an annotation which is @Component because we want a bean of this class to be created by the Spring framework. Followed by we are going to mention another class level annotation which is @RequiredArgsConstructor. This annotation is coming from the Lombok library.
 * Next, we are going to create a method with the signature 'public void on(CustomerCreatedEvent customerCreatedEvent)'. Check this method docstring for more details.
 * With this now, we have completed all the logic related to the CustomerCreated Event. Very similarly, we need to write the logic for other kinds of events. So, copy and paste the createCustomer 'on' method and modify it to handle the other event objects.
 * */
@Component
@RequiredArgsConstructor
public class CustomerProjection {

    private final ICustomerService iCustomerService;

    /** We want this method to be executed whenever the CustomerCreatedEvent is consumed by the projection class. On top of this method, we need to mention an annotation which is @EventHandler.
     * If you remember, inside the aggregate class - CustomerAggregate, we were mentioning an annotation which is '@EventSourcingHandler' which was/is taking care of storing the data in an ES style. But here inside the projection class, we need to use this @EventHandler annotation because we want this method to take the responsibility of handling the event object that is going to be consumed from the Event Bus.
     * Inside this method, we need to write the logic to store the data related to the customer into the customer table. For this, we already have a JPA entity class with the name Customer. First, we are going to create the object of this Customer class. Next, using the BeanUtils.copyProperties method, we are going to copy the data from the CustomerCreatedEvent object to the Customer object since the names of the fields are same/are matching and thus all the data from the event object will be copied to the Customer entity object. Finally, we are going to call the createCustomer method of the CustomerService class to save this data into the Read DB.
     * For the Read DB, I want to use the normal DB itself. Since we are already using the H2 DB, we are going to consider the same H2 DB as the Read DB. If required, you can use MySQL, Oracle, MSSQL or PostgreSQL DB as the Read DB based upon your requirements. If you open the application.yaml file of customer ms, you will be able to see that I have defined the H2 DB related configurations and with that what is going to happen is, our H2 DB is going to be used/act as the Read DB.
     * In order to save this data to the DB, first I need to inject the iCustomerService interface as a dependency into this CustomerProjection class. Then using the reference iCustomerService, I am going to call/invoke the createCustomer method of the ICustomerService interface. To this createCustomer method, I am going to pass the Customer entity object. You are going to get a CE because inside the ICustomerService, as of the previous signature, void createCustomer(CustomerDto customerDto); , we were trying to accept the CustomerDto object as an input parameter. Now, instead of accepting the CustomerDto object, we can modify that signature to accept the Customer entity object itself i.e., void createCustomer(Customer customerEntity); Next, you can navigate to CustomerServiceImpl class and there also, I have modified the signature of the createCustomer method to accept the Customer entity object. Check that CustomerServiceImpl class for more details.
     * */
    @EventHandler
    public void on(CustomerCreatedEvent customerCreatedEvent) {
        Customer customerEntity = new Customer();
        BeanUtils.copyProperties(customerCreatedEvent,customerEntity);
        iCustomerService.createCustomer(customerEntity);
    }

    /** This time the event object that we want to catch is CustomerUpdatedEvent.
     * And btw, it is not always mandatory to follow the style of creating an object using the constructor and then using BeanUtils.copyProperties method to copy the data from the event object to the entity object. We also have other alternative ways with which you can follow or different developers may follow. For example, a developer may decide to send the event object details as is to the respective ServiceImpl method as illustrated for this UpdateCustomerEvent in the CustomerServiceImpl class.
     * Here, we are going to invoke the updateCustomer method of the ICustomerService interface. To this updateCustomer method, I am going to pass the CustomerUpdatedEvent object instead of what we had defined in the signature earlier i.e., boolean updateCustomer(CustomerDto customerDto);
     * As a next step, inside the CustomerServiceImpl - check its docstring for more details.
     * */
    @EventHandler
    public void on(CustomerUpdatedEvent customerUpdatedEvent) {
//        Customer customerEntity = new Customer();
//        BeanUtils.copyProperties(customerCreatedEvent,customerEntity);
        iCustomerService.updateCustomer(customerUpdatedEvent);
    }

    /** This time the event object that we want to catch is CustomerDeletedEvent.
     * And as a next step, inside the CustomerServiceImpl - check its docstring for more details.
     * */
    @EventHandler
    public void on(CustomerDeletedEvent customerDeletedEvent) {
        iCustomerService.deleteCustomer(customerDeletedEvent.getCustomerId());
    }
}
