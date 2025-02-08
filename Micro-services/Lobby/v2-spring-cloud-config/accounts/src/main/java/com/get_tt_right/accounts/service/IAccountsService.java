package com.get_tt_right.accounts.service;

import com.get_tt_right.accounts.dto.CustomerDto;

/**
 * This interface provides the methods to perform CRUD operations on the Accounts.
 * The I prefix is a naming convention for interfaces. Its a standard followed by many projects.
 * The reason we didn't follow this for the repository layer is because we will not have any implementation classes.
 * If a method is super complex, its a good recommendation to add comments as part of the docstring explaining what the method does so that in future if someone is reading the code, they can understand the method without going through the code.
 */
public interface IAccountsService {

    /**
     * Responsible for creating a new account.
     * @param customerDto - Input CustomerDto Object
     */
    void createAccount(CustomerDto customerDto);

    /**
     *
     * @param mobileNumber - Input Mobile Number
     * @return Accounts Details based on a given mobileNumber
     */
    CustomerDto fetchAccount(String mobileNumber);

    /**
     *
     * @param customerDto - CustomerDto Object
     * @return boolean indicating if the update of Account details is successful or not
     */
    boolean updateAccount(CustomerDto customerDto);

    /**
     *
     * @param mobileNumber - Input Mobile Number
     * @return boolean indicating if the delete of Account details is successful or not
     */
    boolean deleteAccount(String mobileNumber);


}