package com.get_tt_right.accounts.mapper;

import com.get_tt_right.accounts.dto.AccountsDto;
import com.get_tt_right.accounts.entity.Accounts;

public class AccountsMapper {

    /**
     * Maps the given Accounts object to an AccountsDto object.
     *
     * @param accounts       - The Accounts object to be mapped.
     * @param accountsDto    - The AccountsDto object to be populated with values from the given Accounts object.
     *
     * @return The populated AccountsDto object.
     */
    public static AccountsDto mapToAccountsDto(Accounts accounts, AccountsDto accountsDto) {
        accountsDto.setAccountNumber(accounts.getAccountNumber());
        accountsDto.setAccountType(accounts.getAccountType());
        accountsDto.setBranchAddress(accounts.getBranchAddress());
        return accountsDto;
    }

    /**
     * Maps the given AccountsDto object to an Accounts object.
     *
     * @param accountsDto - The AccountsDto object containing data to be mapped.
     * @param accounts    - The Accounts object to be populated with values from the given AccountsDto object.
     *
     * @return The populated Accounts object.
     */
    public static Accounts mapToAccounts(AccountsDto accountsDto, Accounts accounts) {
        accounts.setAccountNumber(accountsDto.getAccountNumber());
        accounts.setAccountType(accountsDto.getAccountType());
        accounts.setBranchAddress(accountsDto.getBranchAddress());
        return accounts;
    }

}