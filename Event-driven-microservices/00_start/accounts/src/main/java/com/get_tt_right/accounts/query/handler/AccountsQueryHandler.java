package com.get_tt_right.accounts.query.handler;

import com.get_tt_right.accounts.dto.AccountsDto;
import com.get_tt_right.accounts.query.FindAccountQuery;
import com.get_tt_right.accounts.service.IAccountsService;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

/** Inside this class we are going to have the code which is responsible for handling the query of type FindAccountQuery
 * */
@Component
@RequiredArgsConstructor
public class AccountsQueryHandler {
    private final IAccountsService iAccountsService;

    @QueryHandler
    public AccountsDto findAccount(FindAccountQuery query) {
        AccountsDto account = iAccountsService.fetchAccount(query.getMobileNumber());
        return account;
    }
}
