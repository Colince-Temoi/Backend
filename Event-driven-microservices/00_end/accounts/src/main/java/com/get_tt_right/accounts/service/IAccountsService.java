package com.get_tt_right.accounts.service;

import com.get_tt_right.accounts.dto.AccountsDto;
import com.get_tt_right.common.dto.MobileNumberUpdateDto;

public interface IAccountsService {

    /**
     *
     * @param mobileNumber - Input Mobile Number
     */
    void createAccount(String mobileNumber);

    /**
     *
     * @param mobileNumber - Input Mobile Number
     * @return Accounts Details based on a given mobileNumber
     */
    AccountsDto fetchAccount(String mobileNumber);

    /**
     *
     * @param accountsDto - AccountsDto Object
     * @return boolean indicating if the update of Account details is successful or not
     */
    boolean updateAccount(AccountsDto accountsDto);

    /**
     *
     * @param accountNumber - Input Account Number
     * @return boolean indicating if the delete of Account details is successful or not
     */
    boolean deleteAccount(Long accountNumber);

    /**
     * @param mobileNumberUpdateDto - MobileNumberUpdateDto object
     * @return boolean indicating if the update of mobile number is successful or not
     */
    boolean updateMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto);

    /**
     * @param mobileNumberUpdateDto - MobileNumberUpdateDto object
     * @return boolean indicating if the update of mobile number is successful or not
     */
    boolean rollbackMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto);

}
