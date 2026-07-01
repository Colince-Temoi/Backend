package com.get_tt_right.customer.command.aggregate;

import com.get_tt_right.common.event.CustomerDataChangedEvent;
import com.get_tt_right.customer.command.CreateCustomerCommand;
import com.get_tt_right.customer.command.DeleteCustomerCommand;
import com.get_tt_right.customer.command.UpdateCustomerCommand;
import com.get_tt_right.customer.command.event.CustomerCreatedEvent;
import com.get_tt_right.customer.command.event.CustomerDeletedEvent;
import com.get_tt_right.customer.command.event.CustomerUpdatedEvent;
import com.get_tt_right.customer.entity.Customer;
import com.get_tt_right.customer.exception.CustomerAlreadyExistsException;
import com.get_tt_right.customer.exception.ResourceNotFoundException;
import com.get_tt_right.customer.repository.CustomerRepository;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.util.List;
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
     *
     * Whatever validations we are doing here is nothing but the most basic style of handling the validations inside the Axon framework. With this basic style as we have implemented, we are trying to perform validations immediately the Command object is dispatched to the handler. Though there is no specific disadvantage with this basic style/approach, but it is generally recommended by the Axon framework team to implement these validations before dispatching of the respective command object to the handler logic. There is an interface with the name, 'MessageDispatchInterceptor' inside the Axon framework using which you can define an implementation class, and you can override the handle abstract method. With that what is going to happen is, whenever a command is being dispatched with the help of CommandGateway, just before handing over the command object to the respective handler method in the Aggregate class, whatever logic that you are going to define inside your interceptor is going to be executed.
     * So, as a next step what we will do is we will move this validation logic to the interceptor implementation class. To create an interceptor impl class, under the command package I am going to create a new sub-package with the name "interceptor". Under this interceptor sub-package I am going to create a new class with the name "CustomerCommandInterceptor". Check out the CustomerCommandInterceptor class for more details.
     *
     * Materialized View Pattern Impl
     * --------------------------------
     * Just like we are creating the CustomerCreatedEvent object, what we are going to do is, we are going to create a new object of CustomerDataChangedEvent. Once this object is created, using the same BeanUtils#copyProperties method, I am going to copy the data from the CreateCustomerCommand object to the CustomerDataChangedEvent object. Once the CustomerDataChangedEvent event object is ready we are going to disburse the event. How? By Invoking the apply method and behind the scenes, the event object is going to be dispatched once the "@EventSourcingHandler" method is executed/handled. This we already know. But with the help of apply method, we will be able to dispatch/publish ONLY one event object which we have already done i.e., the CustomerCreatedEvent object. How about the other event that we have? Let's now understand how to dispatch multiple events. If you go to the apply method that we are trying to invoke, you will see that it is going to return an object of type ApplyMore. Using this ApplyMore we should be able to invoke many other methods available to dispatch more event objects. If you check on the members of ApplyMore(I) by entering into it and clicking ctrl + f12 , you will see there are 4 more different methods - you can always read the documentation of these methods to understand when to use them. But in our scenario, we are going to invoke the method which is andThen because we want to dispatch the event without any conditions. As soon as the 1st event is dispatched and processed, immediately we want to dispatch the 2nd event without worrying about any conditions. That's why we are using this method.
     * Now, to this method we need to pass the lambda expression and inside this lambda expression using the AggregateLifecycle#apply method, we are going to pass the CustomerDataChangedEvent object. With this, what is going to happen? Once the very 1st event is dispatched and processed, immediately the 2nd event is going to be dispatched to the event bus. This is just one approach - we will discuss other approaches as well. Next, under the update and delete command handlers as well we need to do the same drill.
     * */
    @CommandHandler
    public CustomerAggregate(CreateCustomerCommand createCustomerCommand, CustomerRepository customerRepository) {
        /*Optional<Customer> optionalCustomer = customerRepository.
                findByMobileNumberAndActiveSw(createCustomerCommand.getMobileNumber(), true);
        if (optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber "
                    + createCustomerCommand.getMobileNumber());
        }*/
        CustomerCreatedEvent customerCreatedEvent = new CustomerCreatedEvent();
        BeanUtils.copyProperties(createCustomerCommand, customerCreatedEvent);
        CustomerDataChangedEvent customerDataChangedEvent = new CustomerDataChangedEvent();
        BeanUtils.copyProperties(createCustomerCommand, customerDataChangedEvent);
        AggregateLifecycle.apply(customerCreatedEvent).andThen(
                () -> AggregateLifecycle.apply(customerDataChangedEvent));
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
     * Reading Data from Event Store
     * -----------------------------
     * In order to read the data from the EventStore, first we need to inject a bean of type EventStore which is provided by the Axon framework to help read data from the Write/Event store DB.
     * Using this "eventStore" reference we can invoke an overloaded method readEvents, the first one is going to accept the aggregateIdentifier as an input - this is going to give all the events associated to an aggregateIdentifier. But if you are looking to read the Write DB from a specific sequence number then you need to invoke the second method which is going to accept the aggregateIdentifier and the sequence number as an input. For our example, I am going to use the first method to read the data from the Write DB.
     * To that method, I am going to pass the aggregateIdentifier which is going to be customerId that we can get from the command object. We need to chain this method to invoke asStream method because we want to read all the events as a stream followed by I want to convert my entire stream of elements into a list. On the LHS, I can try to catch the o/p into type List<?> which is a generic type. Next, we are checking if commands is empty - to imply that there are no records associated to a given customer Id. In such scenarios there is no meaning of performing the update operation and that's why we need to throw a ResourceNotFoundException. The resource here is the "Customer" and the resource id is going to be "customerId".
     * As a next step, we can keep a break point at the line where we are reading the data from the Write DB and we can try to debug this method line by line - of course after saving and building our changes. Now if you try to debug line by line you will see that my List of commands has 3 elements which are nothing but the events which are associated to a given customerId. So, this way you can always read the data from the event store DB. Currently, as we have seen we are just performing the empty check, but if needed you can try to read the data inside these events, and accordingly you can take a decision whether to proceed with your business logic or not. Now, you can release the breakpoint / resume execution and happily you should get a successful response in your postman.
     * With all this we have discussed, you should now be crisp clear on how we can read the data from the Write DB. Since we are trying to perform the same empty check validation/ nothing but checking of a customer is existed with a given mobile number and active switch as true or not inside the CustomerCommandInterceptor class, we are not going to keep this code that we have just discussed here. But for your reference in future on this discussion, I will just comment it out.
     *
     * Materialized View Pattern Impl
     * -------------------------------
     * This time for update command handling, lets see a different approach of publishing the 2nd event to the Event Bus. How? By just invoking the apply method of AggregateLifecycle - as long as you are invoking the apply method of AggregateLifeCycle multiple times with different event object details all those event objects are going to be dispatched by the AggregateLifeCycle. No difference from the very 1st approach that we discussed in the CustomerCreatedEvent handling logic during dispatching a second/multiple event objects. Here, since we are using Axon framework inside our ms's, we are trying to leverage the Axon framework to dispatch the event objects to the Event Bus. But if you are not using Axon framework then you know what to do. In case if you are using Kafka or RabbitMQ inside your Spring boot applications then you can leverage Spring Cloud Stream to dispatch the events to the Kafka Topics or to the Rabbit queues. Inside our ms's sessions we discussed and visualized in detail on how to disburse events to the Kafka Topics or to the Rabbit queues. In case if you have any doubts around the same - you can refresh yourself on the respective sessions related to the kafka or rabbitmq topics.
     * */
    @CommandHandler
    public void handle(UpdateCustomerCommand updateCustomerCommand, EventStore eventStore) {
      /*  List<?> commands = eventStore.readEvents(updateCustomerCommand.getCustomerId()).asStream().toList();
        if(commands.isEmpty()) {
            throw new ResourceNotFoundException("Customer", "customerId", updateCustomerCommand.getCustomerId());
        }*/
        CustomerUpdatedEvent customerUpdatedEvent = new CustomerUpdatedEvent(); // This time the event object that we are creating is going to be of type CustomerUpdatedEvent.
        BeanUtils.copyProperties(updateCustomerCommand, customerUpdatedEvent); // Into the customerUpdatedEvent object, we are going to copy the data from the updateCustomerCommand object as the source.
        CustomerDataChangedEvent customerDataChangedEvent = new CustomerDataChangedEvent();
        BeanUtils.copyProperties(updateCustomerCommand, customerDataChangedEvent);
        AggregateLifecycle.apply(customerUpdatedEvent); // At last, we are publishing the event object. Once this event is published, we need to provide the logic on how this event needs to be stored, inside the write/ES DB. For the same, we need to copy the entire on method from the CustomerCreatedEvent handler method and this time the input to this method is going to be CustomerUpdatedEvent object.
        AggregateLifecycle.apply(customerDataChangedEvent);
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

    /** Materialized View Pattern Impl
     * -------------------------------
     * At last we need to dispatch a 2nd event object from the DeleteCustomerCommand handler method as well. But we can't dispatch an event from this method. Why? Reason : Under the DeleteCustomerCommand we are only accepting the customerId but not the mobile number. haha! If I don't have mobile number then I can't populate the mobile number field inside the CustomerDataChangedEvent object and with this fact - my profile ms will not have any clue to which customer so-and-so event need to be processed.
     * That's why what we are going to so is - instead of dispatching the CustomerDataChangedEvent object from the DeleteCustomerCommand handler method, we are going to dispatch this event object directly from the CustomerServiceImpl class. There you can be able to see a deleteCustomer method impl which is going to be executed to update the active switch flag to false. So, immediately after that operation, we want to trigger an event to the profile ms. But we have a problem? CustomerServiceImpl is not an Aggregator class and that's why we cannot use AggregateLifeCycle#apply method kind of logic here. What is the workaround? Well, we can try to use the EventGateway object that is provided by the Axon framework. How? Inject the EventGateway secondary type as a bean inside the CustomerServiceImpl class.
     * Using this bean, you should be able to publish a single event or a list of events. Check the deleteCustomer method impl for more details.
     * */
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
