package com.get_tt_right.accounts.query.projection;

import com.get_tt_right.accounts.command.event.AccountCreatedEvent;
import com.get_tt_right.accounts.command.event.AccountDeletedEvent;
import com.get_tt_right.accounts.command.event.AccountUpdatedEvent;
import com.get_tt_right.accounts.entity.Accounts;
import com.get_tt_right.accounts.service.IAccountsService;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/** Inside this class as you know we need to write all the logic that is responsible to handle the events that are being published from the command side.
 * We are going to have some CE's because we are yet to make some changes to the IAccountsService interface, and it's respective impl class. For now, you can ignore the CE's as they will be automatically resolved once all the changes are in place as we are following the Read Me.
 * */
@Component
@RequiredArgsConstructor
@ProcessingGroup("account-group")
public class AccountProjection {
    private final IAccountsService iAccountsService;

    @EventHandler
    public void on(AccountCreatedEvent event) {
        Accounts accountEntity = new Accounts();
        BeanUtils.copyProperties(event, accountEntity);
        iAccountsService.createAccount(accountEntity);
    }

    @EventHandler
    public void on(AccountUpdatedEvent event) {
        iAccountsService.updateAccount(event);
    }

    @EventHandler
    public void on(AccountDeletedEvent event) {
        iAccountsService.deleteAccount(event.getAccountNumber());
    }
}
