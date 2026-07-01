package com.get_tt_right.accounts.service;

import com.get_tt_right.accounts.command.event.AccountUpdatedEvent;
import com.get_tt_right.accounts.dto.AccountsDto;
import com.get_tt_right.accounts.entity.Accounts;

/** Here you can see what has changed based on what we have gotten from our instructors' documentation.
 * Now, if you go to the impl class you will need to make some changes to reflect these changes to avoid these CE's.
 * */

public interface IAccountsService {

    /** Old spec
     *
     * @param mobileNumber - Input Mobile Number

    void createAccount(String mobileNumber);
     */

    /** New spec
     *
     * @param account - Accounts Object
     */
    void createAccount(Accounts account);

    /**No changes
     *
     * @param mobileNumber - Input Mobile Number
     * @return Accounts Details based on a given mobileNumber
     */
    AccountsDto fetchAccount(String mobileNumber);

    /** Old Spec
     *
     * @param accountsDto - AccountsDto Object
     * @return boolean indicating if the update of Account details is successful or not

    boolean updateAccount(AccountsDto accountsDto);
     */
    /** New Spec
     *
     * @param event - AccountUpdatedEvent Object
     * @return boolean indicating if the update of Account details is successful or not
     */
    boolean updateAccount(AccountUpdatedEvent event);
    /** No changes
     *
     * @param accountNumber - Input Account Number
     * @return boolean indicating if the delete of Account details is successful or not
     */
    boolean deleteAccount(Long accountNumber);


}
