package com.get_tt_right.customer.command.interceptor;

import com.get_tt_right.customer.command.CreateCustomerCommand;
import com.get_tt_right.customer.command.DeleteCustomerCommand;
import com.get_tt_right.customer.command.UpdateCustomerCommand;
import com.get_tt_right.customer.constants.CustomerConstants;
import com.get_tt_right.customer.entity.Customer;
import com.get_tt_right.customer.exception.CustomerAlreadyExistsException;
import com.get_tt_right.customer.exception.ResourceNotFoundException;
import com.get_tt_right.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

/** On top of this class, first we are mentioning the @Component @RequiredArgsConstructor annotations.
 * Inside this class, we need to implement the interface which is MessageDispatchInterceptor. And to this interface, under the generics symbol we need to type "CommandMessage" which will have a question mark under its generics symbol.
 * Anytime we are trying to dispatch a command, it is going to be dispatched as a "CommandMessage" - if you open this you will notice that it is an interface inside the Axon framework which is going to have the command name, metadata about the command and if you proceed to open the Message interface that it extends, it is going to have more methods like identifier, payload, etc. So basically whatever payload that you are trying to send with the help of your Command object to the framework, it is going to be wrapped into this CommandMessage object. That's why all such command objects we want to intercept and that's why we need to mention this "CommandMessage" under the generics symbol of MessageDispatchInterceptor interface.
 * Now, hover on to this CE on this class and click on the implement methods. Implement the abstract method with the return type BiFunction. With this you will be able to see the method signature that you are going to get from the parent interface MessageDispatchInterceptor. If you navigate to this abstract method in the parent interface you can try to read the documentation of this interface i.e., "Apply this interceptor to the given List of messages...". As of now from the controller layer, we are trying to dispatch a single Command object, but this method as can be seen it accepts a list of Command object messages. Reason : Axon framework also supports batch processing of the commands and so inside the batch processing, since you are going to deal with the collection of Command object messages, to handle such scenarios, this method is accepting list of Command object messages.
 * In our scenario, the List is going to have a single Command object message. If you keep on reading the docstring, you will see that, "This method is going to return a function that can be invoked to obtain a modified version of the Command object message at each position in the List. So, basically, we need to return a lambda function that adheres to the BiFunction functional interface. This lambda expression is going to executed for each of the Command object message that is going to be received by the interceptor. If needed, you can perform the validations, or you can modify the command object message(s). So, its up to you on what you want to write as implementation.
 * */
@Component
@RequiredArgsConstructor
public class CustomerCmdInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {
    private final CustomerRepository customerRepository;
    /** Since this method needs to return a lambda expression I am going to define one here. To this lambda impl, as a very first parameter/variable we need to accept the 'index' which represents the current index value of the command object message that is being processed or that we are looking to process inside the List. Followed by we are going to receive the command object message as an input parameter. And yes the same we need to pass as an input to the lambda expression. For me I mentioned the parameter name as 'command'.
     * Now, from this lambda expression, I need to return the same CommandMessage that I have received after making modifications to it. If I have some validation failures then I can happily throw the respective exception as well. So, to this lambda expression body I am going to write business logic with the help of if conditions. Inside this if condition, I am going to check whether the given Command object message payload class type is equal to the CreateCustomerCommand. To the equals method, I am going to pass the command.getPayloadType(). So, if the given Command object message payload class type is equal to the CreateCustomerCommand object then I want this if block to be executed.
     * Now, inside this if condition, I can do the type casting of the payload into a CreateCustomerCommand object. In the next line, we want to execute our validation logic - this we had already written in the CustomerAggregate constructor, so cut it out and paste it here inside the interceptor. We will get a CE related to the 'customerRepository', this tells us that we need to inject the bean CustomerRepository into this interceptor impl class as a dependency. Like this the error is going to be resolved. Now, since we have written this validation logic here, we can go back to the CustomerAggregate constructor and remove the validation logic there - for me I kept it commented out so that you can see the change trail we did. Like this, you will also notice that we no more need the method parameter named 'customerRepository' in the CustomerAggregate constructor, you can delete it, but I will just let it be for your reference in case if you want to test the commented out code by uncommenting it.
     * Now, once the validation is executed, and you are satisfied that all looks good, and if the condition to check if customer is present is not met, what we want to do is to simply return whatever Command object message we have received. If you have requirements to modify this command message to a new version or if you want to make some modifications, then you can happily write all such logic here. As of now, we just implemented the validation logic for the CreateCustomerCommand. Very similarly, let's tyr to build the conditional blocks for the other 2 commands as well. just copy the same condition multiple times and edit it out to make it work for the other 2 commands.
     * For the second conditional block,I am going to check whether the given CommandMessage is equal to UpdateCustomerCommand. If yes, I am going to do the typecasting to UpdateCustomerCommand. This time inside the UpdateCustomerCommand Validation block, we don't want to use the logic to check if a Customer is existed like we did in the CreateCustomerCommand block, instead, we are going to bring in new logic. First, we are trying to fetch the existing customer based upon the mobile number and active switch as true. If there is no existing record I am throwing a ResourceNotFoundException. Here, the resource is going to be "customer" and the criteria is going to be "mobileNumber". In the last if condition, we can make similar changes as we have done in the UpdateCustomerCommand block. Here, we are checking if there is a given customer record inside the Read DB based upon the given Customer Id and active switch as true. If yes, then proceed, otherwise throw a ResourceNotFoundException. Here, the resource is going to be "customer" and the criteria is going to be "customerId".
     * But as of now, we don't have any method inside the CustomerRepository with the name, "findByCustomerIdAndActiveSw". So, what we can do is copy this method name and go to the CustomerRepository and define/create/mention it to resolve the Compilation issues.
     * With what we have discussed, you should be crisp clear on what we have discussed so far.
     * */
    @Nonnull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(@Nonnull List<? extends CommandMessage<?>> messages) {
        return (index,command) -> {
            if (CreateCustomerCommand.class.equals(command.getPayloadType())) {
                CreateCustomerCommand createCustomerCommand = (CreateCustomerCommand) command.getPayload();
                Optional<Customer> optionalCustomer = customerRepository.
                        findByMobileNumberAndActiveSw(createCustomerCommand.getMobileNumber(), true);
                if (optionalCustomer.isPresent()) {
                    throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber "
                            + createCustomerCommand.getMobileNumber());
                }
            }
            else if (UpdateCustomerCommand.class.equals(command.getPayloadType())) {
                UpdateCustomerCommand updateCustomerCommand = (UpdateCustomerCommand) command.getPayload();
                Customer customer = customerRepository.findByMobileNumberAndActiveSw
                        (updateCustomerCommand.getMobileNumber(), true).orElseThrow(() ->
                        new ResourceNotFoundException("Customer", "mobileNumber", updateCustomerCommand.getMobileNumber()));
            }
            else if (DeleteCustomerCommand.class.equals(command.getPayloadType())) {
                DeleteCustomerCommand deleteCustomerCommand = (DeleteCustomerCommand) command.getPayload();
                Customer customer = customerRepository.findByCustomerIdAndActiveSw(deleteCustomerCommand.getCustomerId(),
                        CustomerConstants.ACTIVE_SW).orElseThrow(() -> new ResourceNotFoundException("Customer", "customerId",
                        deleteCustomerCommand.getCustomerId()));
            }
            return command; // Now, once the validation is executed, and you are satisfied that all looks good, and if the condition to check if customer is present is not met, what we want to do is to simply return whatever Command object message we have received.
        };
    }
}
