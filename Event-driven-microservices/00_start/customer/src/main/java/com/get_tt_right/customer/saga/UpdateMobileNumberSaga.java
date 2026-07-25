package com.get_tt_right.customer.saga;

import com.get_tt_right.common.command.*;
import com.get_tt_right.common.event.*;
//import com.get_tt_right.customer.constants.CustomerConstants;
//import com.get_tt_right.customer.dto.ResponseDto;
//import com.get_tt_right.customer.query.FindCustomerQuery;
//import com.get_tt_right.customer.query.FindUpdateMobileSagaQuery;
import com.get_tt_right.customer.constants.CustomerConstants;
import com.get_tt_right.customer.dto.ResponseDto;
import com.get_tt_right.customer.query.FindCustomerQuery;
import com.get_tt_right.customer.query.FindUpdateMobileSagaQuery;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;

/** On top of this class we are mentioning a class level annotation i.e., @Saga - This is an annotation from the axon framework. If you clan click on this annotation and "download sources" you should be able to read the documentation around this annotation. As can be seen, the documentation confirms that this is an annotation that informs Axon's auto configurer for Spring that a given component is a Saga Instance. By mentioning this annotation we are nothing but letting the Axon framework know that this class is going to act as a Saga Manager. We are also mentioning @Slf4j annotation which is coming from the Lombok library.
 * Inside this class we are going to create a new method which is going to handle the event that we are trying to publish from the CustomerAggregate class. If you go to the CustomerAggregate class, the event that is going to be published is of type CusMobNumUpdatedEvent - See the EventHandler method input parameter. So, the same event, I want to handle inside my SagaManager as well. Just like how we are trying to mention an @EventHandler annotation on top of the Projection class methods, very similarly, here also in the Saga Manager class, we need to mention annotations specific to the Saga event handling mechanism. The annotation is @SagaEventHandler - if you can open it, you will be able to find all the details related to this annotation. If you have any questions/doubts you can always read that documentation.
 *
 * Subscription Queries and response emissions
 * --------------------------------------------
 * There is a catch that we need to be aware of - Whatever overall status that we have developed so far with the help of subscription query, it will only work in the scenarios where the Saga flow is triggered by the Saga manager. You need to think like, what if there is a RTE happens inside my CustomerAggregate class itself while processing this UpdateCusMobNumCommand. Why? The RTE can happen in this layer as well before the request is handed over to the Saga manager. To handle this scenario, what we can do is - we can try to write very similar logic that we have written inside the Saga manager.
 * Inside this class, whenever we are dispatching a command, we are using a kind of logic where we are checking if there is an exception that happened during the command processing - if yes, we are trying to execute some logic. Very similarly, we want to implement the same thing inside the controller class as well. That's why we are copying the entire method#send of CommandGateway from one of the saga event handlers in this class and pasting the same in our CustomerCommandController class updateMobileNumber API just inside our try with resource block. Check that out for more details. No rocket science.
 *
 * */
@Saga
@Slf4j
public class UpdateMobileNumberSaga {

    @Autowired
    private transient CommandGateway commandGateway; // On top of this we are mentioning @Autowired annotation. Nothing but here, we are trying to use field injection because my Axon framework needs an empty default constructor to make this Saga work. That's why here we are trying to use the field level injection. The reason we are using transient here also is - we don't want this object to participate inside the Serialization that is going to happen between the multiple ms's while we are trying to transfer the events and commands among the multiple services.

    @Autowired
    private transient QueryUpdateEmitter queryUpdateEmitter;

    /**So, @SagaEventHandler - it is a method level annotation indicating that the annotated method is an event handler method for the Saga Instance...The 1st parameter is always the payload of the event message(That's what we are trying to accept here in the handle method).
     * As a next step, we need to populate a property value which is associationProperty. To this associationProperty I am going to pass customerId. If you try to read the documentation of this property - It is conveying that this is a property in the event handler that will provide the value to find the Saga instance. Typically, this value is an aggregate identifier of an aggregate that a specific Saga monitors. Basically, as part of your Saga, you are going to trigger and execute and handle lot many events. To give a clue to your Saga manager to find a Saga instance under which this event needs to be handled, we need to populate this associationProperty.
     * Here, I am trying to use the "customerId" as the associationProperty because this is unique for each customer. The same association property with the same value, we need to use in all the other events that we are going to handle with the help of @SagaEventHandler, then only all the events are going to be processed under a single Saga instance. Otherwise, the Saga manager will assume they belong to different Saga instances. With all the changes we have done so far you should be crisp clear. As of now we build a REST API inside the customer ms which is going to publish an event of type CusMobNumUpdatedEvent - Check @CommandHandler method in the CustomerAggregate class for UpdateCusMobNumCommand. If you go to the CustomerAggregate class where we are handling this CusMobNumUpdatedEvent event, and click on the Axon Icon (Orange arrow) >> It will take you to where we are creating CusMobNumUpdatedEvent(Which inside nothing but inside the @CommandHandler method) >> If you click on the orange icon pointing to the LHS of the screen, you will be able to see the event is being handled in 3 places: 1. EventSourcingHandler which is going to take care of updating the data inside the write DB. 2. CustomerProjection which is going to take care of updating the data inside the read DB. 3. The Saga which is going which is going to take care of forwarding this request to the next ms inside the Saga flow.
     * Before we try to write any logic inside this method, 1st we need to make sure we are mentioning an annotation on top of this method which is @StartSaga - Since this is the very 1st Saga event handler inside my saga pattern/flow we need to mention this annotation so that the framework has a clue what is the very 1st event handler that is going to start the saga. Similarly, we will be mentioning @EndSaga annotation on top of the method that is going to handle the last event inside the Saga flow. Once we define this annotation, I am going ahead and defining a logger statement inside the method#handle. With the help of this logger statement, I am trying to log a statement "Saga Event 1 [Start] : Received CusMobNumUpdatedEvent for customerId: {}". These log statements we are adding them for debugging purposes. From this event handler, we want to hand over the request to the next ms which us Account ms. Inside the CQRS pattern, anytime you want to trigger an operation that is going to change the data - you need to do it with the help of command classes. That's why here, I am going to create an object of type UpdateAccntMobileNumCommand. On the RHS, using the builder method I am going to populate all the required fields/details and towards the end I need to invoke the build method. Once the command object is ready, with the help of CommandGateway we can dispatch the command. To use the CommandGateway, we need to inject it as a dependency in this UpdateMobileNumberSaga SagaManager class.
     * Using this CommandGateway, we want to dispatch the command to the accounts ms. As of now so far, we have been leveraging the CommandGateway method#sendAndWait. So to this, we are trying to pass the command object that we have just prepared. With this, what is going to happen is - the command will be dispatched to the accounts ms. But apart from dispatching the command to the accounts ms, in the case of any exception I want to trigger the compensation txn on the customer ms - that is in the case of any RTE. To have that control what we can do is - instead of invoking the method#sendAndWait, we are going to invoke the method#send which is going to accept the command object as the 1st param and as a 2nd parameter I can implement the callback logic that is going to be triggered once the command is executed or processed successfully on the accounts ms. So, as a 2nd parameter I need to implement the business logic that needs to be executed as part of the callback mechanism. That's why here, I am going to type new CommandCallBack<> with empty generics followed by empty parentheses and curly braces. If you go into the CommandCallBack, you will notice that this is an interface. Inside this, you will see there is an abstract method with the name onResult. The same is what we need to implement inside our Saga class. Now, if you go back and hover on the CE, you will get a recommendation to implement that method - so click on it. Now, we can clearly visualize that as part of the 2nd parameter we have a complete anonymous class implementation. So, whatever logic that we are going to write inside this onResult method will be executed once the command is executed successfully.
     * Now, let's imagine what logic we need to write inside this method. Inside this method we need to write a logic to trigger a compensation transaction whenever a RTE happens on the accounts ms. Because we are trying to dispatch the UpdateAccntMobileNumCommand using this method#send, once this command is processed on the accounts ms this command callback implementation is going to be executed. To tell whether my command is processed successfully or not, using the CommandResultMessage reference, we can try to write an if block. Inside the object of CommandResultMessage, there is a method which is isExceptional - this is going to give a boolean, nothing but whether an exception happened or not. If there is an exception happened, we want to trigger/initiate the compensation txn, otherwise everyone is happy and we don't want to trigger any compensation txn. What is the compensation txn that we want to trigger here? We want to trigger the compensation txn on the customer ms. So, there is a command with the name RollbackCusMobNumCommand whose object we need to create and by using the builder method we need to populate the necessary fields/details as can be visualized below. Towards the end, I am also populating the error message field, so that these error message details also we can store inside the write DB. So, how to tell/know the error message? With the help of this same CommandResultMessage object, we can invoke the method exceptionResult followed by getMessage which is going to give you the exception details. Finally towards the end I need to invoke the build method. Now, we have the RollbackCusMobNumCommand command ready which is going to take care of triggering the compensation txn on the customer ms. This command I can try to dispatch with the help of the commandGateway.sendAndWait method.
     * With this, you should now be crisp clear with all the logic that we have written inside this handler method. In summery as of now we have dispatched the UpdateAccntMobileNumCommand to the accounts ms and if there is any exception arise we want to trigger the RollbackCusMobNumCommand on the customer ms. As a next step, we need to go to the account ms and handle this UpdateAccntMobileNumCommand command that we are trying to publish. Check the AccountAggregate class for more details.
     * */
    @StartSaga
    @SagaEventHandler(associationProperty = "customerId")
    public void handle(CusMobNumUpdatedEvent event) {
        log.info("Saga Event 1 [Start] : Received CusMobNumUpdatedEvent for customerId: {}", event.getCustomerId());
        UpdateAccntMobileNumCommand command = UpdateAccntMobileNumCommand.builder()
                .accountNumber(event.getAccountNumber())
                .cardNumber(event.getCardNumber())
                .loanNumber(event.getLoanNumber())
                .customerId(event.getCustomerId())
                .mobileNumber(event.getMobileNumber())
                .newMobileNumber(event.getNewMobileNumber()).build();

//        commandGateway.sendAndWait(command);
        commandGateway.send(command, new CommandCallback<>() {
            @Override
            public void onResult(@Nonnull CommandMessage<? extends UpdateAccntMobileNumCommand> commandMessage,
                    @Nonnull CommandResultMessage<?> commandResultMessage) {
                if (commandResultMessage.isExceptional()) {
                    RollbackCusMobNumCommand rollbackCusMobNumCommand = RollbackCusMobNumCommand.builder()
                            .customerId(event.getCustomerId())
                            .mobileNumber(event.getMobileNumber())
                            .newMobileNumber(event.getNewMobileNumber())
                            .errorMsg(commandResultMessage.exceptionResult().getMessage()).build();
                    commandGateway.sendAndWait(rollbackCusMobNumCommand);
                }
            }
        });

    }

    /** Inside this method, we need to write very similar logic to forward the request to the next ms which is the card ms and in the case of any exceptions we need to trigger the compensation txn as well. Same drill no rocket science.
     * */
    @SagaEventHandler(associationProperty = "customerId")
    public void handle(AccntMobileNumUpdatedEvent event) {
        log.info("Saga Event 2 : Received AccntMobileNumUpdatedEvent for accountNumber: {}", event.getAccountNumber());
        UpdateCardMobileNumCommand command = UpdateCardMobileNumCommand.builder()
                .accountNumber(event.getAccountNumber())
                .cardNumber(event.getCardNumber())
                .loanNumber(event.getLoanNumber())
                .customerId(event.getCustomerId())
                .mobileNumber(event.getMobileNumber())
                .newMobileNumber(event.getNewMobileNumber()).build();
        commandGateway.send(command, new CommandCallback<>() {
            @Override
            public void onResult(@Nonnull CommandMessage<? extends UpdateCardMobileNumCommand> commandMessage,
                    @Nonnull CommandResultMessage<?> commandResultMessage) {
                if (commandResultMessage.isExceptional()) {
                    RollbackAccntMobNumCommand rollbackAccntMobNumCommand = RollbackAccntMobNumCommand.builder()
                            .accountNumber(event.getAccountNumber()) // We are also populating the account number as well as it is required by the accounts ms to perform the rollback in both the read and write DB. If you go inside the RollbackAccntMobNumCommand, you should be able to see this field.
                            .customerId(event.getCustomerId())
                            .mobileNumber(event.getMobileNumber())
                            .newMobileNumber(event.getNewMobileNumber())
                            .errorMsg(commandResultMessage.exceptionResult().getMessage()).build();
                    commandGateway.sendAndWait(rollbackAccntMobNumCommand);
                }
            }
        });
    }

    /** Same drill as we have done before no rocket science - this is a method which is going to handle the events that are going to be dispatched from the CardAggregate class. In this case - CardMobileNumUpdatedEvent is the event that we are going to handle.
     * The request/command we are forwarding to the next ms which is loans ms. This is the last ms in the Saga flow.
     * */
    @SagaEventHandler(associationProperty = "customerId")
    public void handle(CardMobileNumUpdatedEvent event) {
        log.info("Saga Event 3 : Received CardMobileNumUpdatedEvent for cardNumber: {}", event.getCardNumber());
        UpdateLoanMobileNumCommand command = UpdateLoanMobileNumCommand.builder()
                .accountNumber(event.getAccountNumber())
                .cardNumber(event.getCardNumber()) // Apart from customerId, cardNumber and accountNumber, we are also populating the loan number as well.
                .loanNumber(event.getLoanNumber())
                .customerId(event.getCustomerId())
                .mobileNumber(event.getMobileNumber())
                .newMobileNumber(event.getNewMobileNumber()).build();
        commandGateway.send(command, new CommandCallback<>() {
            @Override
            public void onResult(@Nonnull CommandMessage<? extends UpdateLoanMobileNumCommand> commandMessage,
                    @Nonnull CommandResultMessage<?> commandResultMessage) {
                if (commandResultMessage.isExceptional()) {
                    RollbackCardMobNumCommand rollbackCardMobNumCommand = RollbackCardMobNumCommand.builder()
                            .cardNumber(event.getCardNumber())
                            .accountNumber(event.getAccountNumber())
                            .customerId(event.getCustomerId())
                            .mobileNumber(event.getMobileNumber())
                            .newMobileNumber(event.getNewMobileNumber())
                            .errorMsg(commandResultMessage.exceptionResult().getMessage()).build();
                    commandGateway.sendAndWait(rollbackCardMobNumCommand);
                }
            }
        });
    }

    /** Handling the event that is being published by the loan ms i.e. LoanMobileNumUpdatedEvent. This time we are not doing the same drill as we have been doing before as there is a reason haha! This is the last event that is going to happen inside my Saga flow. After the Loans ms, there is no other ms to forward the request to. That's why I am not going to dispatch any commands.
     * Here, I am simply going to add a log statement that the Saga Event 4 has been triggered which is the end of the Saga flow.
     * Also, since this is the method that is going to handle the end of the Saga flow, we need to mention an annotation which is @EndSaga. If required, you can also dispatch another Event or Command from this method based upon your business request/flow/requirement. But for now we should be good.
     * As of now, we have written all the logic related to the Happy path scenario of the Saga flow. Even though we are trying to dispatch the Rollback commands as seen above, we are yet to handle them. Nothing but no where we have written any logic yet on what should happen when these Rollback commands are dispatched. That is what we will be doing next.
     *
     * Subscription Queries - Emitting response to the subscription query.
     * --------------------------------------------------------------------
     * This is the method which will be executed in the scenario of happy path - this we already know. That is if all the txns inside your Saga are successful.
     * So after the log statement, with the help of queryUpdateEmitter I can invoke the method#emit and to it we need to pass the query class name as the 1st param. So, the query class name that we have used while writing the subscription query is - FindUpdateMobileSagaQuery. This you can clear see in the CustomerCommandController in the line of code try(SubscriptionQueryResult<ResponseDto,ResponseDto> queryResult = queryGateway.subscriptionQuery(new FindUpdateMobileSagaQuery(), ResponseTypes.instanceOf(ResponseDto.class),ResponseTypes.instanceOf(ResponseDto.class))) {...}
     * That same class name we need to mention as the 1st param of the method#emit. The 2nd param we need to pass a predicate which is going to be executed on the query payload that we are trying to send from the subscription query. If you check with the CustomerCommandController class for the line of code try(SubscriptionQueryResult<ResponseDto,ResponseDto> queryResult = queryGateway.subscriptionQuery(new FindUpdateMobileSagaQuery(), ResponseTypes.instanceOf(ResponseDto.class),ResponseTypes.instanceOf(ResponseDto.class))) {...}, there you will see we are trying to send the payload of type FindUpdateMobileSagaQuery() as can be noted in the 1st param of the method#subscriptionQuery. Right now it is am empty/marker class but if you have some requirement to send some payload data in it then you can pass some data inside the object of this class so that it stops being a marker class. By using the predicate function, we can apply some filtering conditions on the query object that we are receiving as part of the method#subscriptionQuery 1st param and method#emit 2nd param(predicate input). In our scenario, since we are getting an empty object what we can do is, we will try to write a simple predicate condition which is always going to return true.
     * Under the 3rd param of the method#emit, we need to emit the response that we want to send to the subscriptionQuery. If you can check the subscription query that we have written in the CustomerCommandController, the response should be of type ResponseDto, that's why here in the 3rd param, what we are going to do is - We will try to create the object of type ResponseDto and to its constructor we can pass 2 constants from the CustomerConstants class, one being status 200 indicating that the overall Saga pattern is successful and the other one being another constant conveying the overall message.
     * With the changes we have discussed whenever this @EndSaga method for the happy flow is being executed, we are going to emit a response to the subscription query that is waiting for my response inside my CustomerCommandController. Since there we are using the blockFirst method, the thread is going to wait until I emit the 1st response using the queryUpdateEmitter. Just like how we have discussed emitting the response in the scenario of the happy path, very similarly, we need to emit a response in the failure scenario as well. That's why in the 2nd @EndSaga where we are trying to end the saga flow for the exception path we are going to do the same drill. Check it out for more details.
     * */
    @EndSaga
    @SagaEventHandler(associationProperty = "customerId")
    public void handle(LoanMobileNumUpdatedEvent event) {
        log.info("Saga Event 4 [END] : Received LoanMobileNumUpdatedEvent for loanNumber: {}", event.getLoanNumber());
        queryUpdateEmitter.emit(FindCustomerQuery.class, query -> true,
                new ResponseDto(CustomerConstants.STATUS_200, CustomerConstants.MOBILE_UPD_SUCCESS_MESSAGE));
//        queryUpdateEmitter.emit(FindUpdateMobileSagaQuery.class, query -> true,
//                new ResponseDto(CustomerConstants.STATUS_200, CustomerConstants.MOBILE_UPD_SUCCESS_MESSAGE));
    }

    /** As part of this Saga event handler we want to trigger the next compensation txn by creating the command object of the RollbackAccntMobNumCommand class.
     * Similar rollback logic that we have written in the cards ms aggregate and projection class we need to write in the accounts ms aggregate class. No rocket science.
     * */
    @SagaEventHandler(associationProperty = "customerId")
    public void handle(CardMobNumRollbackedEvent event) {
        log.info("Saga Compensation Event : Received CardMobNumRollbackedEvent for cardNumber: {}", event.getCardNumber());
        RollbackAccntMobNumCommand rollbackAccntMobNumCommand = RollbackAccntMobNumCommand.builder()
                .accountNumber(event.getAccountNumber())
                .customerId(event.getCustomerId())
                .mobileNumber(event.getMobileNumber())
                .newMobileNumber(event.getNewMobileNumber())
                .errorMsg(event.getErrorMsg()).build();
        commandGateway.send(rollbackAccntMobNumCommand); // Like this, we are dispatching the command object to the accounts ms. We can use the method#send or the method#sendAndWait - any of them should work just fine.
    }

    /** Inside this method we need to dispatch the command object of the RollbackCusMobNumCommand class because the previous ms for the accounts ms is the customer ms.
     * Compared to the method above, here we are not setting the account number. Reason: We no more need this!
     * Same drill, in the customer aggregate class, just copy the command handler and the event sourcing handler from the RollbackAccntMobNumCommand and the AccntMobNumRollbackedEvent handle and on methods and modify accordingly.
     * */
    @SagaEventHandler(associationProperty = "customerId")
    public void handle(AccntMobNumRollbackedEvent event) {
        log.info("Saga Compensation Event : Received AccntMobNumRollbackedEvent for accountNumber: {}", event.getAccountNumber());
        RollbackCusMobNumCommand rollbackCusMobNumCommand = RollbackCusMobNumCommand.builder()
                .customerId(event.getCustomerId())
                .mobileNumber(event.getMobileNumber())
                .newMobileNumber(event.getNewMobileNumber())
                .errorMsg(event.getErrorMsg()).build();
        commandGateway.send(rollbackCusMobNumCommand);
    }

    /** Inside this method we don't have to dispatch any of the commands because customer ms is the last ms in the Saga flow where we need to execute the compensation logic.
     * Here, we are just mentioning a simple log statement conveying the fact that this is the end of the Saga flow. And since this method is going to handle the last saga event handler during the compensation/exception flow, we need to mention @EndSaga annotation on top of this method as well.
     * Always you are going to have 2 methods with @EndSaga annotation. One for the happy path flow and one for the compensation/exception flow. Like this we made all the required changes - next we will try to test them in the next session.
     *
     * Subscription Queries - Emitting response to the subscription query.
     * --------------------------------------------------------------------
     * This is the method which is going to be invoked once all the compensation txns are executed.
     * Here, we are instead sending the 500 status with the message mobile_upd_failure_message.
     * */
    @EndSaga
    @SagaEventHandler(associationProperty = "customerId")
    public void handle(CusMobNumRollbackedEvent event) {
        log.info("Saga Compensation Event [END] : Received CusMobNumRollbackedEvent for customerId: {}",
                event.getCustomerId());
        queryUpdateEmitter.emit(FindCustomerQuery.class, query -> true,
                new ResponseDto(CustomerConstants.STATUS_500, CustomerConstants.MOBILE_UPD_FAILURE_MESSAGE));
//        queryUpdateEmitter.emit(FindUpdateMobileSagaQuery.class, query -> true,
//                new ResponseDto(CustomerConstants.STATUS_500, CustomerConstants.MOBILE_UPD_FAILURE_MESSAGE));
    }

}