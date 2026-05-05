package com.get_tt_right.accounts.service.impl;

import com.get_tt_right.accounts.constants.AccountsConstants;
import com.get_tt_right.accounts.dto.AccountsDto;
import com.get_tt_right.accounts.entity.Accounts;
import com.get_tt_right.accounts.exception.AccountAlreadyExistsException;
import com.get_tt_right.accounts.exception.ResourceNotFoundException;
import com.get_tt_right.accounts.mapper.AccountsMapper;
import com.get_tt_right.accounts.repository.AccountsRepository;
import com.get_tt_right.accounts.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
/** This is a very important class - inside this class only I hve written all the business logic concerning the accounts.
 * */
@Service
@AllArgsConstructor
public class AccountsServiceImpl  implements IAccountsService {

    private AccountsRepository accountsRepository;

    /**
     * @param mobileNumber - String
     *                     During the create account operation we are going to receive the mobile number. From this mobile number first we are going to check if there is an account with the given mobile number and active switch as true.
     *                     If there is such an account then we are going to throw a business exception - AccountAlreadyExistsException. Otherwise, we are going to create a brand now account by trying to invoke a private method with the help of the given mobile number.
     */
    @Override
    public void createAccount(String mobileNumber) {
        Optional<Accounts> optionalAccounts= accountsRepository.findByMobileNumberAndActiveSw(mobileNumber,
                AccountsConstants.ACTIVE_SW);
        if(optionalAccounts.isPresent()){
            throw new AccountAlreadyExistsException("Account already registered with given mobileNumber "+mobileNumber);
        }
        accountsRepository.save(createNewAccount(mobileNumber));
    }

    /**
     * @param mobileNumber - String
     * @return the new account details
     * Here we are trying to generate a random account number. Followed by we are trying to set the account type as Savings, branch address as "Address" and active switch as true.
     * At last that entire new entity is going to be passed to the save method which is going to insert a new record inside the DB.
     */
    private Accounts createNewAccount(String mobileNumber) {
        Accounts newAccount = new Accounts();
        newAccount.setMobileNumber(mobileNumber);
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);
        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        newAccount.setActiveSw(AccountsConstants.ACTIVE_SW);
        return newAccount;
    }

    /**
     * @param mobileNumber - Input Mobile Number
     * @return Accounts Details based on a given mobileNumber
     * Will help you fetch the account details based upon the given mobile number. But whatever account that you are trying to fetch it has to be in active state/status. If someone deleted it and marked it as inactive then you are going to get a ResourceNotFoundException.
     */
    @Override
    public AccountsDto fetchAccount(String mobileNumber) {
        Accounts account = accountsRepository.findByMobileNumberAndActiveSw(mobileNumber, AccountsConstants.ACTIVE_SW)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "mobileNumber", mobileNumber)
        );
        AccountsDto accountsDto = AccountsMapper.mapToAccountsDto(account, new AccountsDto());
        return accountsDto;
    }

    /**
     * @param accountsDto - AccountsDto Object
     * @return boolean indicating if the update of Account details is successful or not
     * Will help us to update already existing account details. First we have to load the existing account details from the DB using the mobile number and the active switch as true.
     * Once the accounts record is loaded, we will try to map the new data from the accountsDto object to accounts entity object using the mapToAccounts method - check it out to see what logic is there.
     * Once the account mapper logic is executed, we are passing the same to the save method which is going to take care of update operation.
     */
    @Override
    public boolean updateAccount(AccountsDto accountsDto) {
        Accounts account = accountsRepository.findByMobileNumberAndActiveSw(accountsDto.getMobileNumber(),
                AccountsConstants.ACTIVE_SW).orElseThrow(() -> new ResourceNotFoundException("Account", "mobileNumber",
                accountsDto.getMobileNumber()));
        AccountsMapper.mapToAccounts(accountsDto, account);
        accountsRepository.save(account);
        return  true;
    }

    /**
     * @param accountNumber - Input Account Number
     * @return boolean indicating if the delete of Account details is successful or not
     * Will accept the account number as an input. This method will first check if there is any such account number - of yes it is going to mark the active switch as false so that the account details will be soft-deleted.
     * In real projects we should never try to hard delete the entire record - we should always instead soft delete them with the help of columns like active switch.
     */
    @Override
    public boolean deleteAccount(Long accountNumber) {
        Accounts account = accountsRepository.findById(accountNumber).orElseThrow(
                () -> new ResourceNotFoundException("Account", "accountNumber", accountNumber.toString())
        );
        account.setActiveSw(AccountsConstants.IN_ACTIVE_SW);
        accountsRepository.save(account);
        return true;
    }


}
