package com.get_tt_right.accounts.command.interceptor;

import com.get_tt_right.accounts.command.CreateAccountCommand;
import com.get_tt_right.accounts.command.DeleteAccountCommand;
import com.get_tt_right.accounts.command.UpdateAccountCommand;
import com.get_tt_right.accounts.constants.AccountsConstants;
import com.get_tt_right.accounts.entity.Accounts;
import com.get_tt_right.accounts.exception.AccountAlreadyExistsException;
import com.get_tt_right.accounts.exception.ResourceNotFoundException;
import com.get_tt_right.accounts.repository.AccountsRepository;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

/** This is going to be very similar as to what we have discussed as part the customer ms.
 * Here, we are getting a CE because inside the AccountsRepository we have not defined the method#findByAccountNumberAndActiveSw. So, copy that and go to the AccountsRepository and define it.
 * */
@Component
@RequiredArgsConstructor
public class AccountsCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {
    private final AccountsRepository accountsRepository;

    @Nonnull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(@Nonnull List<? extends
                CommandMessage<?>> messages) {
        return (index, command) -> {
            if (CreateAccountCommand.class.equals(command.getPayloadType())) {
                CreateAccountCommand createAccountCommand = (CreateAccountCommand) command.getPayload();
                Optional<Accounts> optionalAccounts = accountsRepository.findByMobileNumberAndActiveSw(
                        createAccountCommand.getMobileNumber(), true);
                if (optionalAccounts.isPresent()) {
                    throw new AccountAlreadyExistsException("Account already created with given mobileNumber "
                            + createAccountCommand.getMobileNumber());
                }
            } else if (UpdateAccountCommand.class.equals(command.getPayloadType())) {
                UpdateAccountCommand updateAccountCommand = (UpdateAccountCommand) command.getPayload();
                Accounts account = accountsRepository.findByMobileNumberAndActiveSw
                        (updateAccountCommand.getMobileNumber(), true).orElseThrow(() ->
                        new ResourceNotFoundException("Account", "mobileNumber", updateAccountCommand.getMobileNumber()));
            } else if (DeleteAccountCommand.class.equals(command.getPayloadType())) {
                DeleteAccountCommand deleteAccountCommand = (DeleteAccountCommand) command.getPayload();
                Accounts account = accountsRepository.findByAccountNumberAndActiveSw(deleteAccountCommand.getAccountNumber(),
                        AccountsConstants.ACTIVE_SW).orElseThrow(() -> new ResourceNotFoundException("Account", "accountNumber",
                        deleteAccountCommand.getAccountNumber().toString()));
            }
            return command;
        };
    }
}
