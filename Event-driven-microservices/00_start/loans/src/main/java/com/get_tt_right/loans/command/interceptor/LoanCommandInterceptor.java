package com.get_tt_right.loans.command.interceptor;

import com.get_tt_right.loans.command.CreateLoanCommand;
import com.get_tt_right.loans.command.DeleteLoanCommand;
import com.get_tt_right.loans.command.UpdateLoanCommand;
import com.get_tt_right.loans.constants.LoansConstants;
import com.get_tt_right.loans.entity.Loans;
import com.get_tt_right.loans.exception.LoanAlreadyExistsException;
import com.get_tt_right.loans.exception.ResourceNotFoundException;
import com.get_tt_right.loans.repository.LoansRepository;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

@Component
@RequiredArgsConstructor
public class LoanCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {
    private final LoansRepository loansRepository;

    @Nonnull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(@Nonnull List<? extends
                CommandMessage<?>> messages) {
        return (index, command) -> {
            if (CreateLoanCommand.class.equals(command.getPayloadType())) {
                CreateLoanCommand createLoanCommand = (CreateLoanCommand) command.getPayload();
                Optional<Loans> optionalLoans = loansRepository.findByMobileNumberAndActiveSw(
                        createLoanCommand.getMobileNumber(), true);
                if (optionalLoans.isPresent()) {
                    throw new LoanAlreadyExistsException("Loan already created with given mobileNumber "
                            + createLoanCommand.getMobileNumber());
                }
            } else if (UpdateLoanCommand.class.equals(command.getPayloadType())) {
                UpdateLoanCommand updateLoanCommand = (UpdateLoanCommand) command.getPayload();
                Loans loan = loansRepository.findByMobileNumberAndActiveSw
                        (updateLoanCommand.getMobileNumber(), true).orElseThrow(() ->
                        new ResourceNotFoundException("Loan", "mobileNumber", updateLoanCommand.getMobileNumber()));
            } else if (DeleteLoanCommand.class.equals(command.getPayloadType())) {
                DeleteLoanCommand deleteLoanCommand = (DeleteLoanCommand) command.getPayload();
                Loans loan = loansRepository.findByLoanNumberAndActiveSw(deleteLoanCommand.getLoanNumber(),
                        LoansConstants.ACTIVE_SW).orElseThrow(() -> new ResourceNotFoundException("Loan", "loanNumber",
                        deleteLoanCommand.getLoanNumber().toString()));
            }
            return command;
        };
    }
}
