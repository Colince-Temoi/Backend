package com.get_tt_right.loans.command.aggregate;

import com.get_tt_right.common.event.LoanDataChangedEvent;
import com.get_tt_right.loans.command.CreateLoanCommand;
import com.get_tt_right.loans.command.DeleteLoanCommand;
import com.get_tt_right.loans.command.UpdateLoanCommand;
import com.get_tt_right.loans.command.event.LoanCreatedEvent;
import com.get_tt_right.loans.command.event.LoanDeletedEvent;
import com.get_tt_right.loans.command.event.LoanUpdatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
@Slf4j
public class LoanAggregate {
    @AggregateIdentifier
    private Long loanNumber;
    private String mobileNumber;
    private String loanType;
    private String loanStatus;
    private int totalLoan;
    private int amountPaid;
    private int outstandingAmount;
    private boolean activeSw;

    private final String LOAN_APPROVAL_DEADLINE = "loan-approval-deadline-";

    public LoanAggregate() {
    }

    @CommandHandler
    public LoanAggregate(CreateLoanCommand createCommand) {
        LoanCreatedEvent loanCreatedEvent = new LoanCreatedEvent();
        BeanUtils.copyProperties(createCommand, loanCreatedEvent);
        // MV impl
        LoanDataChangedEvent loanDataChangedEvent = new LoanDataChangedEvent();
        BeanUtils.copyProperties(createCommand, loanDataChangedEvent);
        AggregateLifecycle.apply(loanCreatedEvent).andThen(
                () -> AggregateLifecycle.apply(loanDataChangedEvent));
    }

    @EventSourcingHandler
    public void on(LoanCreatedEvent loanCreatedEvent) {
        this.loanNumber = loanCreatedEvent.getLoanNumber();
        this.mobileNumber = loanCreatedEvent.getMobileNumber();
        this.loanType = loanCreatedEvent.getLoanType();
        this.loanStatus = loanCreatedEvent.getLoanStatus();
        this.totalLoan = loanCreatedEvent.getTotalLoan();
        this.amountPaid = loanCreatedEvent.getAmountPaid();
        this.outstandingAmount = loanCreatedEvent.getOutstandingAmount();
        this.activeSw = loanCreatedEvent.isActiveSw();
    }

    @CommandHandler
    public void handle(UpdateLoanCommand updateCommand) {
        LoanUpdatedEvent loanUpdatedEvent = new LoanUpdatedEvent();
        BeanUtils.copyProperties(updateCommand, loanUpdatedEvent);
        // MV impl
        LoanDataChangedEvent loanDataChangedEvent = new LoanDataChangedEvent();
        BeanUtils.copyProperties(updateCommand, loanDataChangedEvent);
        AggregateLifecycle.apply(loanUpdatedEvent);
        AggregateLifecycle.apply(loanDataChangedEvent);
    }

    @EventSourcingHandler
    public void on(LoanUpdatedEvent loanUpdatedEvent) {
        this.loanType = loanUpdatedEvent.getLoanType();
        this.totalLoan = loanUpdatedEvent.getTotalLoan();
        this.amountPaid = loanUpdatedEvent.getAmountPaid();
        this.outstandingAmount = loanUpdatedEvent.getOutstandingAmount();
    }

    @CommandHandler
    public void handle(DeleteLoanCommand deleteCommand) {
        LoanDeletedEvent loanDeletedEvent = new LoanDeletedEvent();
        BeanUtils.copyProperties(deleteCommand, loanDeletedEvent);
        AggregateLifecycle.apply(loanDeletedEvent);
    }

    @EventSourcingHandler
    public void on(LoanDeletedEvent loanDeletedEvent) {
        this.activeSw = loanDeletedEvent.isActiveSw();
    }
}
