package com.get_tt_right.accounts.command.aggregate;

import com.get_tt_right.accounts.command.CreateAccountCommand;
import com.get_tt_right.accounts.command.DeleteAccountCommand;
import com.get_tt_right.accounts.command.UpdateAccountCommand;
import com.get_tt_right.accounts.command.event.AccountCreatedEvent;
import com.get_tt_right.accounts.command.event.AccountDeletedEvent;
import com.get_tt_right.accounts.command.event.AccountUpdatedEvent;
import com.get_tt_right.common.command.RollbackAccntMobNumCommand;
import com.get_tt_right.common.command.RollbackCardMobNumCommand;
import com.get_tt_right.common.command.UpdateAccntMobileNumCommand;
import com.get_tt_right.common.event.AccntMobNumRollbackedEvent;
import com.get_tt_right.common.event.AccntMobileNumUpdatedEvent;
import com.get_tt_right.common.event.AccountDataChangedEvent;
import com.get_tt_right.common.event.CardMobNumRollbackedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

/** Here, we should be able to easily understand this code.
 * First we have all the primary fields that we are going to store using this Aggregate with the accountNumber going to act as an aggregate identifier.
 * Followed by an explicitly defined empty constructor which is required by the axon framework.
 *
 * Orchestration Saga Pattern impl for update mobile number Saga flow.
 * --------------------------------------------------------------------
 * Here we need to define a new command handler and event sourcing handler methods. Same drill, just copy the command handler and the event sourcing handler from the DeleteAccountCommand and the AccountDeletedEvent handle and on methods and modify accordingly. Check them out for more details.
 *
 * */
@Aggregate(snapshotTriggerDefinition = "accountSnapshotTrigger")
public class AccountsAggregate {
    @AggregateIdentifier
    private Long accountNumber;
    private String mobileNumber;
    private String accountType;
    private String branchAddress;
    private boolean activeSw;
    private String errorMsg;

    public AccountsAggregate() {
    }

    /** For the create customer command we are going to leverage this constructor.
     * */
    @CommandHandler
    public AccountsAggregate(CreateAccountCommand createCommand) {
        AccountCreatedEvent accountCreatedEvent = new AccountCreatedEvent();
        BeanUtils.copyProperties(createCommand, accountCreatedEvent);
        // MV impl
        AccountDataChangedEvent accountDataChangedEvent = new AccountDataChangedEvent();
        BeanUtils.copyProperties(accountCreatedEvent, accountDataChangedEvent);
        AggregateLifecycle.apply(accountCreatedEvent).andThen(
                () -> AggregateLifecycle.apply(accountDataChangedEvent));
    }

    /** For the create customer command we are going to leverage this event sourcing handler which is going to handle this account created event.
     * */
    @EventSourcingHandler
    public void on(AccountCreatedEvent accountCreatedEvent) {
        this.accountNumber = accountCreatedEvent.getAccountNumber();
        this.mobileNumber = accountCreatedEvent.getMobileNumber();
        this.accountType = accountCreatedEvent.getAccountType();
        this.branchAddress = accountCreatedEvent.getBranchAddress();
        this.activeSw = accountCreatedEvent.isActiveSw();
    }

    @CommandHandler
    public void handle(UpdateAccountCommand updateCommand) {
        AccountUpdatedEvent accountUpdatedEvent = new AccountUpdatedEvent();
        BeanUtils.copyProperties(updateCommand, accountUpdatedEvent);
        // MV impl
        AccountDataChangedEvent accountDataChangedEvent = new AccountDataChangedEvent();
        BeanUtils.copyProperties(accountUpdatedEvent, accountDataChangedEvent);
        AggregateLifecycle.apply(accountUpdatedEvent);
        AggregateLifecycle.apply(accountDataChangedEvent);
    }

    @EventSourcingHandler
    public void on(AccountUpdatedEvent accountUpdatedEvent) {
        this.accountType = accountUpdatedEvent.getAccountType();
        this.branchAddress = accountUpdatedEvent.getBranchAddress();
    }

    @CommandHandler
    public void handle(DeleteAccountCommand deleteCommand) {
        AccountDeletedEvent accountDeletedEvent = new AccountDeletedEvent();
        BeanUtils.copyProperties(deleteCommand, accountDeletedEvent);
        AggregateLifecycle.apply(accountDeletedEvent);
    }

    @EventSourcingHandler
    public void on(AccountDeletedEvent accountDeletedEvent) {
        this.activeSw = accountDeletedEvent.isActiveSw();
    }

    /**Orchestration Saga Pattern impl for update mobile number Saga flow.
     * -------------------------------------------------------------------
     * The command that we are handling here is UpdateAccntMobileNumCommand which we are dispatching from the UpdateMobileNumberSaga SagaManager class.
     * As part of this command I want to dispatch a new event of type AccntMobileNumUpdatedEvent. Finally, towards the end we are publishing AccntMobileNumUpdatedEvent event using the AggregateLifecycle.apply method. The same we are now catching with the help of EventSourcingHandler method below. Check it out for more details.
     * */

    @CommandHandler
    public void handle(UpdateAccntMobileNumCommand updateAccntMobileNumCommand) {
        AccntMobileNumUpdatedEvent accntMobileNumUpdatedEvent = new AccntMobileNumUpdatedEvent();
        BeanUtils.copyProperties(updateAccntMobileNumCommand, accntMobileNumUpdatedEvent);
        AggregateLifecycle.apply(accntMobileNumUpdatedEvent);
    }

    /**Orchestration Saga Pattern impl for update mobile number Saga flow.
     * -------------------------------------------------------------------
     * Using the same accntMobileNumUpdatedEvent we want to update the mobile number by invoking the method getNewMobileNumber.
     * The very similar logic we have written isndie the CustomerAggregate class as well and it is the very same thing we are going to write inside the Cards and Loans ms's respective aggregate classes.
     * Next, we need to open the projection class of accounts ms i.e., AccountsProjection which is going to take care of updating the data inside the read DB - check it out for more details.
     * */
    @EventSourcingHandler
    public void on(AccntMobileNumUpdatedEvent accntMobileNumUpdatedEvent) {
        this.mobileNumber = accntMobileNumUpdatedEvent.getNewMobileNumber();
    }

    @CommandHandler
    public void handle(RollbackAccntMobNumCommand rollbackAccntMobNumCommand) {
        AccntMobNumRollbackedEvent accntMobNumRollbackedEvent = new AccntMobNumRollbackedEvent();
        BeanUtils.copyProperties(rollbackAccntMobNumCommand, accntMobNumRollbackedEvent);
        AggregateLifecycle.apply(accntMobNumRollbackedEvent); // Dispatching the event
    }

    /** In this method, we need to write some tricky logic haha! 1st we need to update the old mobile number. Reason: We are trying to rollback using the compensation txn.
     * Followed by I want to also populate the error message inside the write DB to understand the error faced. To achieve this, I will add one more class level primary attribute/field i.e., errorMessage. If you can recall, inside the Saga manager class,we are trying to populate the errorMsg attribute with the help of commandResultMessage.exceptionResult().getMessage(). The same we are trying to save into the write DB.
     * S0, whatever logic we have written here is going to take care of reverting the changes on the account write DB. As a nxt step, we need to go to the account projection class write the rollback logic. Check it out for more details.
     * */
    @EventSourcingHandler // Handling the event dispatched
    public void on(AccntMobNumRollbackedEvent accntMobNumRollbackedEvent) {
        this.mobileNumber = accntMobNumRollbackedEvent.getMobileNumber();
        this.errorMsg = accntMobNumRollbackedEvent.getErrorMsg();
    }

}
