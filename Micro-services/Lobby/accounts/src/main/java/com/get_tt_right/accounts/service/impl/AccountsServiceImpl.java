package com.get_tt_right.accounts.service.impl;

import com.get_tt_right.accounts.constants.AccountsConstants;
import com.get_tt_right.accounts.dto.AccountsDto;
import com.get_tt_right.accounts.dto.CustomerDto;
import com.get_tt_right.accounts.entity.Accounts;
import com.get_tt_right.accounts.entity.Customer;
import com.get_tt_right.accounts.exception.CustomerAlreadyExistsException;
import com.get_tt_right.accounts.exception.ResourceNotFoundException;
import com.get_tt_right.accounts.mapper.AccountsMapper;
import com.get_tt_right.accounts.mapper.CustomerMapper;
import com.get_tt_right.accounts.repository.AccountsRepository;
import com.get_tt_right.accounts.repository.CustomerRepository;
import com.get_tt_right.accounts.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
/**
 * This class implements the IAccountsService interface.
 * This class is responsible for handling the business logic for the accounts.
 * @Field accountsRepository: The repository for the accounts.
 * @Field customerRepository: The repository for the customers.
 */
@Service
@AllArgsConstructor  // If you ctrl +f12 on the class name, you will see the constructor with the fields. And whenever there is only a single constructor in your class that is accepting parameters, you don't need to manually autowire the fields. Spring will automatically inject the dependencies.
public class AccountsServiceImpl  implements IAccountsService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;

    /**
     * Responsible for creating a new account.
     * We are also handling the exceptions here. Think like someone is trying to use the same mobile number twice.
     * If the mobile number is already registered, we will throw an exception. I mean multiple customers can have the same name, email, but not the same mobile number.
     * That's why we are checking if the mobile number is already registered or not.
     * @param customerDto - Input CustomerDto Object
     */
    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        // Framework provides a find by id, PK, method, but we don't have a find by mobile number method. So we need to create a custom finder method in the repository. The reason is because the mobile number is not a primary key and we are not receiving the primary key from the client when creating a new customer.
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if(optionalCustomer.isPresent()) {
            /* Where is the logic to handle this exception?
            * This will be thrown to my parent method - In the controller where we called this method. In the controller we can handle this exception using catch block and return the response to the client.
            * But in future if there is some other place where we are trying to throw this exception, then a different controller method again has to catch and  handle this exception.
            * This imply that we are having a lot of exception handling logic that is going to be duplicated inside your application.
            * To overcome this challenge, as a standard, we need to write a Global Exception Handler.
            * */
            throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber "
                    +customerDto.getMobileNumber());
        }
        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy("System");
        Customer savedCustomer = customerRepository.save(customer); // With this line of code spring data jpa will take care of taking the input object, preparing an SQL statement, creating a connection with the database, executing it, committing the transaction, and closing the connection. All this boilerplate code is taken care of by spring data jpa.
        accountsRepository.save(createNewAccount(savedCustomer)); // createNewAccount method will create a new account and save it to the database.
    }

    /** Responsible for creating a new account.
     * Logic: Create >> Save
     * @param customer - Customer Object
     * @return the new account details
     */
    private Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000); // This will give me a 10 digit random account number.

        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        newAccount.setCreatedAt(LocalDateTime.now());
        newAccount.setCreatedBy("System");
        return newAccount;
    }

    /** Logic: Find >> Return
     * @param mobileNumber - Input Mobile Number
     * @return Accounts Details based on a given mobileNumber
     */
    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );
        // We are combining the customer and account details to DTO classes, because they have some information which is not required in the response (Metadata and other sensitive information),  and sending it to the client.
        // To send this, we have 2 options - Either we can create a new DTO class that aggregates the customer and account details that we need to send to the client or we can use the existing DTO class, CustomerDto, and add the account details, AccountsDto, to it.
        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));
        return customerDto;
    }

    /** Responsible for updating the account details.
     *
     * Logic: Find >> Update
     *
     * We are updating the account details based on the account number. Account number should not be changed/updateable.
     * @param customerDto - CustomerDto Object
     * @return boolean indicating if the update of Account details is successful or not
     */
    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if(accountsDto !=null ){
            Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString())
            );
            AccountsMapper.mapToAccounts(accountsDto, accounts);
            accounts = accountsRepository.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString())
            );
            CustomerMapper.mapToCustomer(customerDto,customer);
            // Save method is a very smart method. It will check if the object's PK is already present in the database or not. If it is present, then it will update the object. If it is not present, then it will insert the object.
            customerRepository.save(customer);
            isUpdated = true;
        }
        return  isUpdated;
    }

    /** Logic: Find >> Delete
     * @param mobileNumber - Input Mobile Number
     * @return boolean indicating if the delete of Account details is successful or not
     */
    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        // Since this deleteByCustomerId finder method is a custom method, we need to make sure we are mentioning 2 annotations - @Transactional and @Modifying.
        // We have written it with our own hands, and we are trying to change data inside the database. Whenever you are trying to do modification to the database, with the custom methods that you have written, we need to make sure we are mentioning 2 annotations - @Transactional and @Modifying.
        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        // You may ask yourself a question that we are also invoking deleteById, why are you not mentioning @Modifying and @Transactional annotations here?
        // It because this method is from the framework. We are not writing this method. This method is provided by the framework. So, the framework will take care of the transaction management.
        customerRepository.deleteById(customer.getCustomerId());
        return true;
    }


}