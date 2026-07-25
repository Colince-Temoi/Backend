package com.get_tt_right.customer.command.controller;

import com.get_tt_right.common.command.RollbackCusMobNumCommand;
import com.get_tt_right.common.command.UpdateAccntMobileNumCommand;
import com.get_tt_right.common.command.UpdateCusMobNumCommand;
import com.get_tt_right.common.dto.MobileNumberUpdateDto;
import com.get_tt_right.customer.command.CreateCustomerCommand;
import com.get_tt_right.customer.command.DeleteCustomerCommand;
import com.get_tt_right.customer.command.UpdateCustomerCommand;
import com.get_tt_right.customer.constants.CustomerConstants;
import com.get_tt_right.customer.dto.CustomerDto;
import com.get_tt_right.customer.dto.ResponseDto;
import com.get_tt_right.customer.query.FindCustomerQuery;
import com.get_tt_right.customer.query.FindUpdateMobileSagaQuery;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nonnull;
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
    private final QueryGateway queryGateway;

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

    /** Creating a new REST API that is of type patch mapping with the path /mobile-number. Next we are creating a method below this annotation which is of the signature +updateMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto): ResponseEntity<ResponseDto>
     * Inside this method we are going to accept a request body of type MobileNumberUpdateDto. Since inside the class MobileNumberUpdateDto we have mentioned some validation annotations, the same we want to execute when this API is invoked and that's why we are using the @Valid annotation. This method returns a ResponseEntity object of type ResponseDto. Using the data present inside this MobileNumberUpdateDto object, we need to create and populate the object of UpdateCusMobNumCommand. Inside this UpdateCusMobNumCommand class, there is a builder method and using the same we are going to populate all the data. Towards the end I need to invoke the build method which is going to create the object of UpdateCusMobNumCommand.
     * Next, just like how we are dispatching the command as seen in the above methods, we need to do the same here - so just copy and paste. When the command us dispatched, where do we need to handle it? Inside the aggregate class i.e., CustomerAggregate.java. Check this out for more details.
     *
     * Subscription Queries.
     * ---------------------
     * Just before dispatching the command i.e., just before the line of code, commandGateway.sendAndWait(updateCusMobNumCommand); What we will do is, we will try to invoke a query but to invoke a query we need to inject QueryGateway dependency in this class. With the help of this QueryGateway, I am going to invoke the method#subscriptionQuery. To this method, 1st  we need to provide the object of the query class that we have just created now i.e. FindUpdateMobileSagaQuery. This will be the very 1st parameter. Followed by, inside the 2nd parameter, we need to tell what is the return data type that we are expecting as part of the initial state response. Like we discussed previously, the subscription query is capable of sharing the initial state output followed by further updates. So, under this 2nd parameter, I can mention the return datatype that I am expecting under the initial state response - For this, let us use the ResponseTypes class then on top of it, invoke the instanceOf method. In this instanceOf method I will mention the return datatype which I am expecting which is ResponseDto.class. So, this is the initial state response that I am expecting.
     * Similarly, I can mention what is the return datatype that I am expecting for the subsequent updates that I am going to receive from the subscription query. For the subsequent updates also I am going to expect the same data type - that's why I am trying to mention the 2nd parameter as of type ResponseDto.class as can be seen in the 3rd parameter of subscriptionQuery. If you open this method#subscriptionQuery you will get to know the details about this method. The very 1st param indicates the type of the query, the 2nd param indicates the type of the initial response and finally the 3rd param indicates the type of the incremental update that we are going to receive. With what we have done so far you should be crisp clear. On the LHS, I need to catch the output with the help of SubscriptionQueryResult. Since we are expecting 2 types of updates (Initial update and incremental updates) we need in a generics, mention the 2 types that we are expecting as a response/result/return types from the subscriptionQuery. So, we need in the generics to mention <ResponseDto,ResponseDto> only. Reason: On the RHS we have mentioned ResponseDto as a 2nd parameter and 3rd parameter respectively. Followed by we are mentioning the variable name as "queryResult".
     * Now, using this "queryResult" variable I can do the magic. towards the end of this method, we were initially trying to return a new ResponseDto object as can be seen commented. Instead of that, to the body method I can pass the output that I am catching/that I am getting in the queryResult. So, using queryResult invoke a method#updates() and then after this invoke the method#blockFirst(). With this blockFirst command, what is going to happen is - the thread that is executing this method is going to wait until it receives the 1st response of type ResponseDto. If needed we can invoke the method#blockFirst that accepts Duration as an input which mean that your thread is only going to wait for the duration that you have provided. In my scenario, I want to wait indefinitely and that's why I am trying to invoke the plain method. As a developer I need to make sure I am closing the connection of the queryResult as well before I try to exit from this method. To close the resource what I can do is, I can leverage try with resources block and inside that try parenthesis I can move the code i.e., SubscriptionQueryResult<ResponseDto,ResponseDto> queryResult = queryGateway.subscriptionQuery(new FindUpdateMobileSagaQuery(), ResponseTypes.instanceOf(ResponseDto.class),ResponseTypes.instanceOf(ResponseDto.class))
     * Then in the open and close curly braces, I can move the entire subsequent lines of code including the return statement.
     * With this, what is going to happen is, whenever my try block execution is completed the queryResult resource is going to be automatically closed. So far, we have just created a subscription query which is going to wait for the very first update. As a next step, we need to publish/emmit the 1st update towards the end of the Saga flow so that we can send the same update/response to the client application. Inside our Saga manager class i.e., UpdateMobileNumberSaga, Inside this class there are only 2 methods that have @EndSaga annotation. This @EndSaga annotations indicates that it is going to end the Saga flow. These methods are the right places to emit the overall status to the client application. That's why we are going to make changes inside these methods. Check them out for more details. Before we try to make any changes to these methods, 1st we need to make sure we are injecting a bean of type QueryUpdateEmitter using which we can be able to emit the response to the subscription query.
     * With the changes we have made, now my client application is going to wait for the overall status.
     * Now, there is a catch that we need to be aware of - Whatever overall status that we have developed so far with the help of subscription query, it will only work in the scenarios where the Saga flow is triggered by the Saga manager. You need to think like, what if there is a RTE happens inside my CustomerAggregate class itself while processing this UpdateCusMobNumCommand. Why? The RTE can happen in this layer as well before the request is handed over to the Saga manager. To handle this scenario, what we can do is - we can try to write very similar logic that we have written inside the Saga manager. Check out the Saga Manager class docstring for more details.
     * For the new commandGateway.send(...) logic to dispatch the command that we have introduced here due to the catch we have discussed in the previous line, is going to have some CEs. Just do some modifications to resolve the same. I.e., the command name should be updateCusMobNumCommand. In the generics of CommandMessage we need to update the correct command class name i.e.,UpdateCusMobNumCommand. Followed by we don't want to roll back/ compensate anything here, that's why in the if block what we want to do is - we are going to return the overall response indicating internal server error. If you see, with the help of ResponseEntity, we are trying to populate the status and the body. Under the body I am trying to send the ResponseDto object with the status code as 500 and the message I am going to send whatever message I am going to receive from the Exception result of the CommandResultMessage. This way the client application is always going to have a proper response.
     * After making all these changes, toward the end we can get rid of the line of code commandGateway.sendAndWait(updateCusMobNumCommand); Save the changes do a build and retest your services to verify the introduced changes are working as expected - Nothing but to ensure that the subscription query results are loaded properly.
     * */
    @PatchMapping("/mobile-number")
    public ResponseEntity<ResponseDto> updateMobileNumber(@Valid @RequestBody MobileNumberUpdateDto mobileNumberUpdateDto) {
        UpdateCusMobNumCommand updateCusMobNumCommand = UpdateCusMobNumCommand.builder()
                .customerId(mobileNumberUpdateDto.getCustomerId())
                .accountNumber(mobileNumberUpdateDto.getAccountNumber())
                .loanNumber(mobileNumberUpdateDto.getLoanNumber())
                .cardNumber(mobileNumberUpdateDto.getCardNumber())
                .mobileNumber(mobileNumberUpdateDto.getCurrentMobileNumber())
                .newMobileNumber(mobileNumberUpdateDto.getNewMobileNumber()).build();

//        try(SubscriptionQueryResult<ResponseDto,ResponseDto> queryResult = queryGateway.subscriptionQuery(new FindUpdateMobileSagaQuery(), ResponseTypes.instanceOf(ResponseDto.class),ResponseTypes.instanceOf(ResponseDto.class))) {
          try(SubscriptionQueryResult<ResponseDto,ResponseDto> queryResult = queryGateway.subscriptionQuery(
                  new FindCustomerQuery(mobileNumberUpdateDto.getNewMobileNumber()),
                  ResponseTypes.instanceOf(ResponseDto.class),ResponseTypes.instanceOf(ResponseDto.class))) {
            commandGateway.send(updateCusMobNumCommand, new CommandCallback<>() {
                @Override
                public void onResult(@Nonnull CommandMessage<? extends UpdateCusMobNumCommand> commandMessage,
                                     @Nonnull CommandResultMessage<?> commandResultMessage) {
                    if (commandResultMessage.isExceptional()) {
                        ResponseEntity
                                .status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new ResponseDto(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR.name(),
                                        commandResultMessage.exceptionResult().getMessage()));
                    }
                }
            });

//            commandGateway.sendAndWait(updateCusMobNumCommand); // Like this we are going to dispatch the command of type UpdateCusMobNumCommand
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(new ResponseDto(CustomerConstants.STATUS_200, CustomerConstants.MESSAGE_200));
            return ResponseEntity.status(HttpStatus.OK)
                    .body(queryResult.updates().blockFirst());
        }


    }

}
