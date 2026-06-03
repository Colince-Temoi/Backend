package com.get_tt_right.customer.command.aggregate;

import com.get_tt_right.customer.command.CreateCustomerCommand;
import com.get_tt_right.customer.command.DeleteCustomerCommand;
import com.get_tt_right.customer.command.UpdateCustomerCommand;
import com.get_tt_right.customer.command.event.CustomerCreatedEvent;
import com.get_tt_right.customer.command.event.CustomerDeletedEvent;
import com.get_tt_right.customer.command.event.CustomerUpdatedEvent;
import com.get_tt_right.customer.entity.Customer;
import com.get_tt_right.customer.exception.CustomerAlreadyExistsException;
import com.get_tt_right.customer.repository.CustomerRepository;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.util.Optional;

/*** On top of this class we need to mention an annotation which is @Aggregate - It is an annotation from the axon framework. By mentioning this class level annotation we are telling to the axon framework that this class is going to act as an aggregator which is responsible to process all the command that we are going to trigger from the controller layer - CustomerCommandController class. From this CustomerCommandController class we are just trying to assemble the inputs into a command object and publishing the command with the help of CommandGateway. So, this published command we need to handle inside the CustomerAggregate class.
 * Apart from handling the commands, this class is also responsible to store the given data using ES pattern. Once the storing of the data is completed, it is also responsible to trigger the event so that the data will be synced on the read DB as well. Since this class is also responsible to store the data inside the Event Store DB, in it,  we need to define/mention/create the member fields that are related to all the data that we want to store inside the Write DB. If you can go to the CreateCustomerCommand class, we have some fields all of which we want to store the respective data inside the Event Sourcing DB or Write DB. That's why I am going to copy these those fields and mention/paste them inside the CustomerAggregate class as members. The only thing we should not do is mentioning them as final because everytime someone tries to update the customer data, we want to leverage these fields to update the data inside the Write DB. That's why we should not mark these fields as final.
 * As a next step on top of this customerId field, we need to mention a property level annotation which is @AggregateIdentifier. This tells to the axon server what is the field name that the axon framework can use whenever it wants to make updates to the existing record related to a customer. We know crisp clear that very first time a brand-new record/customer is going to be created in the write DB, but from the next time onwards whenever someone is trying to perform some update/delete/modify operation, the framework what it is going to do is - first it is going to try to load the existing data from the write DB and once it understand the existing record information then only it is going to write the new data inside the write DB in an Event Sourcing style. What is going to happen inside ES pattern? We are not going to have a single record which is going to be modified everytime, instead everytime a modify operation happens on a given existing customer record, the framework is going to make a new entry of the same customer record but with the new changes. And whenever this new entry is being made - the framework has to understand what are the previous entries already made, and accordingly it is going to increment the sequence number so that using the sequence number at any point of time the framework will understand the sequence of the events that happened on a given customer record. To help the framework on loading the existing records from the write DB we should choose a field which is not going to change throughout the life cycle of the customer record. So, we need to mention a property level annotation which is @AggregateIdentifier on that field. We know very well that customerId is such a field which is going to be created during the creation time of a customer record and the same will never change. The other fields i.e., mobileNumber, email and name are going to change over the time.
 * For example, if a customer initially created a record with the mobile number as 123 and later if they want to change the mobile number to 456 then in such scenario, the whole event sourcing pattern is not going to work because you have some events with the mobile number as 123 and some events with the mobile number as 456. That's why it is always VERY important to chose a field that is never going to change as an AggregateIdentifier.
 * Once you define/mention these member fields, you need to create a default constructor of this class explicitly for the framework purpose. Followed by we need to create a parameterized constructor that is going to accept the command that we are going to trigger from the Command layer - CustomerCommandController. The command that we are trying to trigger from this layer is the CreateCustomerCommand. The same we need to pass as an input to this parameterized constructor and inside this constructor only we are going to write the logic on how this command is to be handled.
 *
 * While we were trying to handle the CreateCustomerCommand, we have created a parameterized constructor of this CustomerAggregate class. With this it should sink in your brain that we should only leverage this constructor during the create operation. For the update and delete operations, we should define normal Java methods. That's why what I am going to do is - after @EventSourcingHandler method, I am going to create a method which is - handle - Check out its docstring to understand more details.
 * Very similarly, we need to create the logic to handle the DeleteCustomerCommand. Same way as we did for UpdateCustomerCommand in the detailed discussion - just change the input command from UpdateCustomerCommand to DeleteCustomerCommand.
 */
@Aggregate
public class CustomerAggregate {
    @AggregateIdentifier
    private String customerId;
    private String name;
    private String email;
    private String mobileNumber;
    private boolean activeSw;

    public CustomerAggregate() {}

    /** On top of this constructor we should mention an annotation which is @CommandHandler since we are expecting this constructor to handle this command of type CreateCustomerCommand. That's why it is important to mention this constructor level annotation so that the axon framework will understand which constructor it needs to invoke to handle this command during the create operation.
     * Now inside this constructor, we can write the logic on how the event need to be published on the query side. Once the event is published, we should also take care of storing these customer details inside the write/Event Sourcing DB. Before we try to write all such logic, we need to perform some validations just to make sure that there is no active customer already present with a given mobile number. We should NOT! allow multiple customers with the same mobile number. That's why what we are going to do here is, from this constructor we are going to query the read DB to understand if there is already a given customer with the given mobile number. If there is no record, then ONLY! we will proceed with the command processing otherwise we will throw a meaningful error.
     * Since we are looking to Look UP the data inside the read DB, we are going to leverage the CustomerRepository that we already have. So, pass this CustomerRepository reference as a second input to this constructor. When we pass this CustomerRepository (I) reference as an input to this constructor, the respective bean is going to be automatically injected as a dependency to this CustomerAggregate class. Now, inside the body of this constructor, I am going to invoke a method which is findByMobileNumberAndActiveSw on top of the customerRepository reference. To this method, I need to pass the mobile number that I have received from the command,followed by 'true' boolean value. On the RHS I am going to catch the response/output by using Optional of type Customer with the variable name as optionalCustomer.
     * Next, we are going to check if there is any customer inside this Optional Customer object and that's why we are going to use the if condition i.e., if(optionalCustomer.isPresent()) - if there is already a customer existed with a given mobile number we are going to throw a new exception which is CustomerAlreadyExistsException and to this I am going to pass a message which is "Customer already registered with given mobileNumber "  and towards the end I am appending the mobile number that I have received from the command/command object i.e., "+ createCustomerCommand.getMobileNumber(). So, this is a simple validation that we are trying to do before we try to process the command. Once I am good with all my validations, as a next step I need to trigger an event associated to this command from this constructor. For this, we need to create an object of CustomerCreatedEvent. If you can recall our previous discussions, this is the corresponding event class for the command which is CreateCustomerCommand. Once the object of this event class is created, we need to copy all the data present inside the command object to the event object.
     * To copy the data from one object to another object, I am going to leverage the BeanUtils available inside the Spring framework using which I am going to invoke the copyProperties method and to this method I am going to pass the command object (Source object) and the event object (Target/destination object) as inputs. What this copyProperties method is going to do is, it is going to copy the data from the command class to the event class by looking at the field names - nothing but if the field names are matching, then the data will be copied from source to destination. Like this now, we have the event object created, populated with data and readily available. As a next step, we need to publish this event(s) so that the data will be stored inside the write DB and once the data is stored, the same event will be published to the event bus as well. So, to publish/trigger this event, we need to leverage a class which is AggregateLifecycle - inside this class we have a method which is apply() and to this method I am going to pass the event object as an input. With this, what we are trying to do? We are only publishing the event. Once the event is published, the axon framework is going to look for the logic on how to store the data inside this event object in the write DB/ ES DB. Once that logic is executed, the axon framework is going to publish the event to the event bus.
     * So, as a next step, we need to write the event sourcing logic - Check this just after this constructor as we are going to define a new method which is 'on' - actually the method name can be anything haha!
     * Hope you are seeing and connecting with the patterns. Whenever we are trying to handle a command, first we can perform any validations on the command object, followed by we need to copy the validate command object to the event object, followed by we need to publish the event object to the axon framework, and finally we need to write the event sourcing logic. So, once the event object is published to the framework, the framework is going to look for the logic present inside the respective @EventHandler method definition, and it is going to execute the logic, and then store the data inside the write/ES DB. Once this data is written into the ES/Write DB, the framework is going to finally subsequently publish the respective event object to the event bus.
     * So, as of now, we have completed everything on the command side. As a next step we should try to build the Projection on the Query side which is going to consume the event object from the event bus and store the given data to the read DB.
     *  */
    @CommandHandler
    public CustomerAggregate(CreateCustomerCommand createCustomerCommand, CustomerRepository customerRepository) {
        Optional<Customer> optionalCustomer = customerRepository.
                findByMobileNumberAndActiveSw(createCustomerCommand.getMobileNumber(), true);
        if (optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber "
                    + createCustomerCommand.getMobileNumber());
        }
        CustomerCreatedEvent customerCreatedEvent = new CustomerCreatedEvent();
        BeanUtils.copyProperties(createCustomerCommand, customerCreatedEvent);
        AggregateLifecycle.apply(customerCreatedEvent);
    }

    /** This method is going to accept the event of type CustomerCreatedEvent, and it is going to populate the fields of this aggregate class with the data present inside this event object.
     * This method does not return anything. On top of this method we need to mention an annotation which is @EventSourcingHandler - Why? We need to tell to the framework that whenever this CustomerCreatedEvent object is published from the aggregate class like we saw in the constructor above the logic - AggregateLifecycle.apply(customerCreatedEvent);, then we want this method to catch the event object and store the data into the write/ES DB. Once this writing of the Event object data to the write/ES DB is done, the framework is going to take care of publishing this event further into the Event Bus.
     * Inside this method, the logic that we are going to write is going to be very simple - we just have to read the data from the Event object and populated into the primary fields that we have mentioned/defined inside this aggregate class. To refer to the fields present inside the same CustomerAggregate class, we need to use the 'this' keyword.
     * Simple logic! haha! We just have to write that code and behind the scenes, what the axon framework is going to do is, it is going to look for the storage DB and inside the Storage DB all these event object details will be stored in an historical/sequential style. Long back in our slides we discussed and visualized on how the EventSourcing pattern storage is going to happen. At the every bottom, you are going to have the every first event - so, whenever the customer is being created, similarly, at the very bottom - the very first event is going to be created. Similarly, when the modify(update/patch/delete) operations are happening to this created customer record, all those subsequent events are going to be written on top of the existing events.
     * At any point in time if you look in the storage DB, we are going to have all the sequence of events that are happened on this given customer record. That's the advantage of ES pattern.
     * But you may have a burning question which is - as of now, we have not configured any write DB for our Axon framework to store the customer data? To answer the same, at the start of our discussions you can remember we started the Axon server in dev mode. Like this the storage of data related to all the events, will happen inside the 'events' folder that we have created under the axonserver folder on the Desktop. Long back, while we were trying to start the axon server container you can remember we mount these folders to the docker container - so any "events" and "data" that is being generated by the axon server, all such data and events will be stored inside these respective folders.
     * In production, what DevOps team members are going to do is - they are going to configure a proper write DB like a Mongo DB or any other DB which is capable of handling the ES pattern. Axon team also recommend using Mongo DB or any other NoSQL DB. For now, for our local development, whatever data that is going to be stored inside the respective folders we mounted to our docker container should be good enough for your understanding of the concepts.
     * So, with the logic that we have written, the data will be stored inside the "write/ES DB". Once the storing of the data is completed the event object will be published into the event bus. As you can visualize in our illustration in the slides, we have the event bus in the middle and like this you may have another burning question which is - We have not configured any Kafka or RabbitMQ to accept and process the event objects that we are publishing? haha! Great question! To clarify this, whatever axon server that we set up with the help of docker container is capable of having an Event Bus internally and it is going to take care of processing these event objects using an Event Bus. But if required - you can configure a RabbitMQ or Kafka to act as an Event Bus in real projects based upon your project demands/requirements - the DevOps people are going to replace the Axon Server provided Event Bus with Kafka or RabbitMQ or any other Event processing capability product. But for our local development, we should be good with whatever Axon server is providing because in real projects we are not responsible for setting up this Event Bus.
     * We are only responsible to publish the events into the Event Bus and to consume the events from the Event Bus. Simple!
     * */
    @EventSourcingHandler
    public void on(CustomerCreatedEvent customerCreatedEvent) {
        this.customerId = customerCreatedEvent.getCustomerId();
        this.name = customerCreatedEvent.getName();
        this.email= customerCreatedEvent.getEmail();
        this.mobileNumber = customerCreatedEvent.getMobileNumber();
        this.activeSw = customerCreatedEvent.isActiveSw();
    }

    /** This method is going to accept a command of type UpdateCustomerCommand as an input. On top of this method we need to mention a method level annotation which is @CommandHandler. Whenever we want to make a constructor or a method to handle a command, we need to use this method/constructor level annotation on that respective method or constructor.
     * As usual, if needed the first thing we can do is to perform some validations based upon the data that we have received inside the command object input. Otherwise, we can proceed to the next steps which are creating the event, copying the data and invoke the apply method of the AggregateLifecycle class.
     * For now, I am going to skip the validations part inside the UpdateCustomerCommand handler method because in the coming sessions we will visualize a better way on how to define the validations using an Interceptor class instead of defining all the validation logic inside aggregate class respective handler methods/constructor. Instead we can keep all that validation logic inside an Interceptor class.
     *
     * */
    @CommandHandler
    public void handle(UpdateCustomerCommand updateCustomerCommand, EventStore eventStore) {
        CustomerUpdatedEvent customerUpdatedEvent = new CustomerUpdatedEvent(); // This time the event object that we are creating is going to be of type CustomerUpdatedEvent.
        BeanUtils.copyProperties(updateCustomerCommand, customerUpdatedEvent); // Into the customerUpdatedEvent object, we are going to copy the data from the updateCustomerCommand object as the source.
        AggregateLifecycle.apply(customerUpdatedEvent); // At last, we are publishing the event object. Once this event is published, we need to provide the logic on how this event needs to be stored, inside the write/ES DB. For the same, we need to copy the entire on method from the CustomerCreatedEvent handler method and this time the input to this method is going to be CustomerUpdatedEvent object.
    }

    /** This method is going to accept an event object of type CustomerUpdatedEvent as an input.
     * On top of this method we need to mention a method level annotation which is @EventSourcingHandler.
     * During the update operation, I don't want to give flexibility to update the customerId, mobileNumber and activeSw fields. I want to update only name and email fields.
     * In the coming sessions we will have a separate operation to update the mobile number across all ms's like customer, accounts, cards and loans.
     * Using update operation here, the end user can only update the name and email fields/details. That's why I have only mentioned these 2 field names here.
     * With this update operation, what is going to happen is - inside the write/ES DB, a new entry will be created and into that new entry the updated name and email details will be updated/modified whereas coming to the other fields like customerId, mobileNumber and activeSw, they will not be updated/modified, they will just be simply copied over from the previous immediate event entry that we have stored inside the ES/Write DB.
     * With what we have discussed so far you should be crisp clear.
     * */
    @EventSourcingHandler
    public void on(CustomerUpdatedEvent customerUpdatedEvent) {
        this.name = customerUpdatedEvent.getName();
        this.email= customerUpdatedEvent.getEmail();
    }

    @CommandHandler
    public void handle(DeleteCustomerCommand deleteCustomerCommand) {
        CustomerDeletedEvent customerDeletedEvent = new CustomerDeletedEvent();
        BeanUtils.copyProperties(deleteCustomerCommand, customerDeletedEvent);
        AggregateLifecycle.apply(customerDeletedEvent);
    }

    /** Since we are only going to soft delete the data, we are going to populate the activeSw field to false boolean value.
     * If you check the CustomerCommandController class, under the delete endpoint, the customerId we are going to receive from the end user, whereas the activeSw we are populating with a false value that we have defined in our constants and the same we are trying to send to our aggregate class.
     * And that's why above, we are simply copying the data from the deleteCustomerCommand object to the customerDeletedEvent object.
     * Now, inside this Event Sourcing handler method, we need to accept the CustomerDeletedEvent object as an input and this time we are only going to patch/update/modify the activeSw field.
     * With this, we have written the code required to handle the DeleteCustomerCommand and the UpdateCustomerCommand.
     *
     * */
    @EventSourcingHandler
    public void on(CustomerDeletedEvent customerDeletedEvent) {
        this.activeSw = customerDeletedEvent.isActiveSw();
    }


}
