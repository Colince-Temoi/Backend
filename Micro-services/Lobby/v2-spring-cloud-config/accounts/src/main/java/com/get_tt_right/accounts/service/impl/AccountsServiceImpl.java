package com.get_tt_right.accounts.service.impl;

import com.get_tt_right.accounts.constants.AccountsConstants;
import com.get_tt_right.accounts.dto.AccountsDto;
import com.get_tt_right.accounts.dto.AccountsMsgDto;
import com.get_tt_right.accounts.dto.CustomerDto;
import com.get_tt_right.accounts.entity.Accounts;
import com.get_tt_right.accounts.entity.Customer;
import com.get_tt_right.accounts.exception.CstomerAlredyExistExcption;
import com.get_tt_right.accounts.exception.ResourceNotFoundException;
import com.get_tt_right.accounts.mapper.AccountsMapper;
import com.get_tt_right.accounts.mapper.CustomerMapper;
import com.get_tt_right.accounts.repository.AccountsRepository;
import com.get_tt_right.accounts.repository.CustomerRepository;
import com.get_tt_right.accounts.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountsServiceImpl  implements IAccountsService {

    private static final Logger log = LoggerFactory.getLogger(AccountsServiceImpl.class);

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private final StreamBridge streamBridge;

    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if(optionalCustomer.isPresent()) {
            throw new CstomerAlredyExistExcption("Customer already registered with given mobileNumber "
                    +customerDto.getMobileNumber());
        }
        Customer savedCustomer = customerRepository.save(customer);
        Accounts savedAccount = accountsRepository.save(createNewAccount(savedCustomer));
        sendCommunication(savedAccount, savedCustomer);
    }

    /** First, I am trying to create an object of AccountsMsgDto record class by passing all the required inputs to the constructor invocation of AccountsMsgDto
     * This object store, we want to send to the message ms. After creating the object, I have some logger statement to intamate that I am sending communication request for the details present in the object store of AccountsMsgDto. After sending the communication request, I have some logger statement to intamate that I am sending communication request for the details present in the object store of AccountsMsgDto that we have just created.
     * Next, with the help of 'streamBridge' reference that we have just defined/injected into this class, we are going to invoke the send method. To this send method, we need to mention what is the output destination binding name that we have defined/configured in the 'application.yml' file. This same binding name, we need to pass a first parameter to this send method. Since we are invoking the send method, it is obvious for the Spring Cloud Stream, that this name that we are passing as a first parameter belongs to an Output binding. And whenever we are using this output binding name, it also knows that what is the exchange name that it has to forward the message to. The exchange name we have defined in the 'application.yml' file with the help of the destination property. Then, we need to pass the object store of AccountsMsgDto that we have just created. This is nothing but the message that we are trying to send.
     * With this, the message will be received by the exchange that is available within the RabbitMQ. With all this, you should be able to establish a link between accounts ms, RabbitMQ and message ms. When we see the demo, its is going to be super clear for you.
     * Now, we are going to return a boolean value on weather the message has been sent successfully sent to the RabbitMQ or not. The same boolean value is what I am trying to print with the log statement seen below.
     * With this we have now made all the required changes in the messages and accounts ms.
     * */
    private void sendCommunication(Accounts account, Customer customer) {
        var accountsMsgDto = new AccountsMsgDto(account.getAccountNumber(), customer.getName(),
                customer.getEmail(), customer.getMobileNumber());
        log.info("Sending Communication request for the details: {}", accountsMsgDto);
        var result = streamBridge.send("sendCommunication-out-0", accountsMsgDto);
        log.info("Is the Communication request successfully triggered ? : {}", result);
    }

    private Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000); // This will give me a 10 digit random account number.

        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        return newAccount;
    }

    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );
        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));
        return customerDto;
    }
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
            customerRepository.save(customer);
            isUpdated = true;
        }
        return  isUpdated;
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        return true;
    }

    /**
     *  Like you can see whatever logic we have written here is super simple business logic. Initializing the isUpdated attribute with a false value, checking if account number is null >> if not null, fetching the account details associated with the account number using the findById behavior because inside my accounts table, the account number is a PK.
     *  If there is no account number, we are simply throwing an exception which is ResourceNotFoundException. Otherwise, we are trying to use the same accounts object that we have fetched from the db and setting the communication switch to true. Post that, we are trying to invoke the save method which will update the communication switch value to true in the db behind the scenes.
     *  At last, we are setting the isUpdated value to true and the same we are trying to return from this method.
     *  As a next step, we need to use this logic inside the consumer function that we have written inside the AccountsFunctions class. Check out that class for more details.
     * */
    @Override
    public boolean updateCommunicationStatus(Long accountNumber) {
        boolean isUpdated = false;
        if(accountNumber !=null ){
            Accounts accounts = accountsRepository.findById(accountNumber).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountNumber.toString())
            );
            accounts.setCommunicationSw(true);
            accountsRepository.save(accounts);
            isUpdated = true;
        }
        return  isUpdated;
    }


}