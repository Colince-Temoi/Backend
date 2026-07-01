package com.get_tt_right.accounts.command.aggregate;

import com.get_tt_right.accounts.command.CreateAccountCommand;
import com.get_tt_right.accounts.command.DeleteAccountCommand;
import com.get_tt_right.accounts.command.UpdateAccountCommand;
import com.get_tt_right.accounts.command.event.AccountCreatedEvent;
import com.get_tt_right.accounts.command.event.AccountDeletedEvent;
import com.get_tt_right.accounts.command.event.AccountUpdatedEvent;
import com.get_tt_right.common.event.AccountDataChangedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

/** Here, we should be able to easily understand this code.
 * First we have all the primary fields that we are going to store using this Aggregate with the accountNumber going to act as an aggregate identifier.
 * Followed by an explicitly defined empty constructor which is required by the axon framework.
 * */
@Aggregate
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

}
