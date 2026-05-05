package com.get_tt_right.accounts.mapper;

import com.get_tt_right.accounts.dto.AccountsDto;
import com.get_tt_right.accounts.entity.Accounts;
/** Here, I have the logic to transfer the data from Dto to entity and vice versa
 * */
public class AccountsMapper {

    public static AccountsDto mapToAccountsDto(Accounts accounts, AccountsDto accountsDto) {
        accountsDto.setAccountNumber(accounts.getAccountNumber());
        accountsDto.setMobileNumber(accounts.getMobileNumber());
        accountsDto.setAccountType(accounts.getAccountType());
        accountsDto.setBranchAddress(accounts.getBranchAddress());
        accountsDto.setActiveSw(accounts.isActiveSw());
        return accountsDto;
    }

    /** Inside this method you can see that I am not giving flexibility to the end-user to update whatever he/she wants - hahah!
     * The end-user cannot update the account number because account number once generated is immutable/should not be updated.
     * The mobile number also I am not allowing to update through this method - but in the coming sessions we are going to write a separate method to update the mobile number. The reason is - we are going to maintain the same mobile number across all the ms's and so, if someone wants to update the mobile number they have to invoke a separate API which is going to take care of updating the same mobile number across all ms's.
     * With that, in accounts ms, I am only going to allow to update the account type and the branch address as part of the update operation in this ms.
     * */
    public static Accounts mapToAccounts(AccountsDto accountsDto, Accounts accounts) {
        accounts.setAccountType(accountsDto.getAccountType());
        accounts.setBranchAddress(accountsDto.getBranchAddress());
        return accounts;
    }

}
