package com.get_tt_right.customer.command.controller;

import com.get_tt_right.customer.command.CreateCustomerCommand;
import com.get_tt_right.customer.command.DeleteCustomerCommand;
import com.get_tt_right.customer.command.UpdateCustomerCommand;
import com.get_tt_right.customer.constants.CustomerConstants;
import com.get_tt_right.customer.dto.CustomerDto;
import com.get_tt_right.customer.dto.ResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * If you recall, we build all the CRUD operation related APIs inside a class - CustomerController. The same logic you need to repeat here in the CustomerCommandController. Once you separate those APIs we had already written inside CustomerController into command and query specific APIs, we are going to delete the CustomerController class.
 * Copy the class level annotations as is in CustomerController class to CustomerCommandController class. Along with those annotations, mention one more annotation which is @RequiredArgsConstructor. Next, we are looking to build an API to create a customer, so, copy the entire createCustomer method along with its method level annotation(s) from CustomerController class to CustomerCommandController class.
 * Check that createCustomer method docstring for more details.
 * Inside the CustomerController, copy the updateCustomerDetails method signature along with its method level annotation(s) from CustomerController class to CustomerCommandController class.
 * Now, inside the CustomerCommandController, copy the deleteCustomer method signature along with its method level annotation(s) from CustomerController class to CustomerCommandController class.
 * Like this, we have developed 3 kind of write APIs that are going to be handled by the command component - create, update and delete. If you see, what is happening right now is, we are sending this commands to the Axon server, but axon server does not have any clue on how these commands should be handled. That's why as a next step we need to build an aggregator component on the command side. What an Aggregators is? We are going to understand this in a few. But of what we have discussed so far you should be crisp clear.
 * Since we have the command APIs inside the CommandController class, we can go ahead and delete/comment out the duplicate APIs that we have inside the CustomerController class. i.e., create, update and delete APIs.
 * */
@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@RequiredArgsConstructor
public class CustomerCommandController {

    private final CommandGateway commandGateway;

    /** As of now, inside this method if you closely observe, we are going to accept the data with the help of this CustomerDto class where we have fields like name, email and mobileNumber - the other 2 fields i.e., customerId and activeSw are optional as end user does not need to send them as we are going to generate them inside our business logic.
     * Inside this method 1st we are trying to create a customerId with the help of this UUID class. Later on we are trying to invoke the method iCustomerService.createCustomer which is present inside the CustomerServiceImpl class - But with that logic, what is going to happen is that the customer data is going to be stored inside the customer DB normally/traditionally like we already used to.
     * But for us we want the data to be stored with the help of Event Sourcing pattern that's why comment/delete the line of code iCustomerService.createCustomer(customerDto); as we are not going to rely on it anymore. Also the line of code i.e., customerDto.setCustomerId(UUID.randomUUID().toString()); we don't need it as well. Once we receive the data from the end user, we need to create and populate the object of CreateCustomerCommand and we need to pass the same to the Axon Server.
     * So, to create the object of the command we are trying to fire, we already have defined an associated command class which is CreateCustomerCommand. Using the same class name, we are going to invoke the builder method as it supports that. Reason: we have mentioned a class level @Builder annotation in the CreateCustomerCommand class. After invoking the builder method, in a chain we can keep on populating all the required data from the customerDto object to the command object by invoking its respective fields.
     * First, we are invoking the customerId field, adn since the customer is not going to invoke the customerId field, we are going to populate it with the help of UUID class by invoking the UUID.randomUUID().toString() method to populate the customerId field. Next, we are going to populate the email, name and mobileNumber fields as we are going to get them from the customerDto object. Finally, we are going to populate the activeSw field with the value defined/mentioned in the constant field - CustomerConstants.ACTIVE_SW.
     * Once all the required data is assembled and populated, towards the end we need to invoke the build method. This build method is actually going to create the object of that respective command class i.e., CreateCustomerCommand by using all the assembled data. Of course, if you don't want to follow this builder pattern approach, there are many ways to create and populate that CreateCustomerCommand object i.e., using the Constructor invocation approach etc. Now, we have our CreateCustomerCommand object ready, but, we need to convey this object to the Axon server - otherwise it is of now use! 😂 How to convey that?
     * It is very easy - Axon Server has a class which is CommandGateway - the same I am trying to inject as a dependency to this CustomerCommandController class as a secondary attribute. Behind the scenes, during the start-up, the Axon framework is going to create the bean of this CommandGateway class - if you navigate to it, you will notice that this CommandGateway type is an interface but behind the scenes, its implementation class bean is going to be created as a bean by the framework and the same we are trying to use as a dependency for this class.
     * So, using this commandGateway attribute, we are going to invoke a method which is 'sendAndWait'. If you want to trigger a command and wait for the command execution to complete, you need to leverage this sendAndWait method. This is going to leverage the blocking style of invocation - nothing but the thread is going to be blocked and it is going to wait for the completion of the command execution. There are multiple overloaded methods of sendAndWait - you can always check on the respective quick documentation in your IDE on what each expects as input, does and the expected output from each. If you simply use the sendAndWait method that takes the command object ONLY as input, it is going to wait INDEFINITELY for the command execution to complete. Whereas if you want you can use the overloaded method alternative to specify how much time you want to wait.
     * There is also another overloaded sendAndWait method that is going to also allow you to provide some metadata information about your command. There is also another alternative overloaded method that is going to allow you to provide metadata information, timeout information along with the command object. For now, we want to wait indefinitely and that's why we are trying to use the sendAndWait method that takes the command object as input only. So, to this method we are passing the object that we have created which is the CreateCustomerCommand object. If you were looking to trigger the command asynchronously in a non-blocking style, you can use the 'send' overloaded method alternatives instead. These send methods, along with the command object as input, they are also going to accept the callback function which is going to be executed when the command execution is complete. But since I don't have any other code to be executed once this command is triggered and executed, I am going to use the sendAndWait method which is going to wait indefinitely for the command execution to complete.
     * So, once the command is processed, behind the scenes the data will be stored inside the write DB. Towards the end, I want to return the 201 status to the end user indicating that the account is created successfully.
     * And like that, we have created the create API. Very similarly, we are going to create the other command APIs i.e., Update and Delete as well. Check out the docstring of those APIs for more details.
     * */
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createCustomer(@Valid @RequestBody CustomerDto customerDto) {
//        customerDto.setCustomerId(UUID.randomUUID().toString());
//        iCustomerService.createCustomer(customerDto);
        CreateCustomerCommand createCustomerCommand = CreateCustomerCommand.builder()
                .customerId(UUID.randomUUID().toString()).email(customerDto.getEmail())
                .name(customerDto.getName()).mobileNumber(customerDto.getMobileNumber())
                .activeSw(CustomerConstants.ACTIVE_SW).build();
        commandGateway.sendAndWait(createCustomerCommand);
        return ResponseEntity
                .status(org.springframework.http.HttpStatus.CREATED)
                .body(new ResponseDto(CustomerConstants.STATUS_201, CustomerConstants.MESSAGE_201));
    }

    /** Inside this method as a very first step, we need to create and populate the object of UpdateCustomerCommand.
     * This time around we are not creating the customerId from scratch - instead we can fetch it from the customerDto object. The other properties like email, name and mobileNumber are going to be populated from the customerDto object. as we did in the createCustomer method. Finally, we need to populate the activeSw field with the value defined/mentioned in the constant field - CustomerConstants.ACTIVE_SW.
     * At last using the commandGateway, we need to invoke the sendAndWait method that takes the UpdateCustomerCommand object as input - this actually triggers the command.
     * Towards the end, we are returning the 200 status to the end user indicating that the account is updated successfully. With this, the update API is created.
     * */
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateCustomerDetails(@Valid @RequestBody CustomerDto customerDto) {
        UpdateCustomerCommand updateCustomerCommand = UpdateCustomerCommand.builder()
                .customerId(customerDto.getCustomerId()).email(customerDto.getEmail())
                .name(customerDto.getName()).mobileNumber(customerDto.getMobileNumber())
                .activeSw(CustomerConstants.ACTIVE_SW).build();
        commandGateway.sendAndWait(updateCustomerCommand);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto(CustomerConstants.STATUS_200, CustomerConstants.MESSAGE_200));
    }

    /** Here, I am trying to use the PatchMapping instead of the DeleteMapping because it is a partial update. Nothing but we are not entirely going to delete the record - instead we are going to soft delete the record.
     * Inside the body of this method signature, I am going to create the object of the DeleteCustomerCommand and populate the customerId field from the request parameter, the activeSw field with the value defined/mentioned in the constant field - CustomerConstants.IN_ACTIVE_SW. Finally, I am going to invoke the sendAndWait method that takes the DeleteCustomerCommand object as input.
     * Towards the end we can pass the same status as we did in the updateCustomerDetails method conveying that the soft delete is successfully completed.
     * */
    @PatchMapping("/delete")
    public ResponseEntity<ResponseDto> deleteCustomer(@RequestParam("customerId")
                                                      @Pattern(regexp = "(^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$)",
                                                              message = "CustomerId is invalid") String customerId) {
        DeleteCustomerCommand deleteCustomerCommand = DeleteCustomerCommand.builder()
                .customerId(customerId).activeSw(CustomerConstants.IN_ACTIVE_SW).build();
        commandGateway.sendAndWait(deleteCustomerCommand);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDto(CustomerConstants.STATUS_200, CustomerConstants.MESSAGE_200));

    }

}
