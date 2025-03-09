package com.get_tt_right.accounts.service.impl;

import com.get_tt_right.accounts.dto.AccountsDto;
import com.get_tt_right.accounts.dto.CustomerDetailsDto;
import com.get_tt_right.accounts.dto.LoansDto;
import com.get_tt_right.accounts.entity.Accounts;
import com.get_tt_right.accounts.entity.Customer;
import com.get_tt_right.accounts.exception.ResourceNotFoundException;
import com.get_tt_right.accounts.mapper.AccountsMapper;
import com.get_tt_right.accounts.mapper.CustomerMapper;
import com.get_tt_right.accounts.repository.AccountsRepository;
import com.get_tt_right.accounts.repository.CustomerRepository;
import com.get_tt_right.accounts.service.client.CardsFeignClient;
import com.get_tt_right.accounts.service.client.LoansFeignClient;
import com.get_tt_right.accounts.service.ICustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/** Update as of 7/3/2025
 * Before I try to write some logic here, first I need to inject all the required inputs/dependencies inside this class. The dependencies here are:
 * 1. I am going to use AccountsRepository to fetch all the Accounts related information from the DB that this Accounts ms is trying to use.
 * 2. I am going to use LoansRepository to fetch all the Loans related information from the DB that this Loans ms is trying to use.
 * 3. I am going to use CardsRepository to fetch all the Cards related information from the DB that this Cards ms is trying to use.
 * 4. I am going to require CustomerRepository to fetch all the Customer related information from the DB that Accounts ms is trying to use.
 *
 * Hold on, for cards and Loans related information, in order to get those details, we need to invoke the other ms's. And whenever we want to invoke other ms's we need to make sure we are using the Feign client related interfaces that we have created and that's why I am trying to inject those interfaces inside this class.
 * So, these are the dependencies that I need to inject inside this class. Make sure to also mention on top of this class 2 annotations i.e., @Service and @AllArgsConstructor. Since I have mentioned the @AllArgsConstructor, lombok will generate a constructor with all the fields(Primary or secondary) that I have defined inside this class.
 *  And whenever we have only a single constructor inside a class, then the autowiring is going to happen automatically without the need of mentioning @Autowired annotation on each of these fields. As a next step, we need to write some logic inside my fetchCustomerDetails behavior. We have already written some logic to fetch Accounts and Customer related information inside the AccountsServiceImpl.
 *  Copy that code  and paste it here as part of the business logic inside this method. Just omit the logic to perform mapping to CustomerDto and AccountsDto along with the return statements. This we will tweak to reflect out requirements. As a next step, I need to map the received customer and accounts data to CustomerDetailsDto. As of now, we don't have any mapper method that that will convert Accounts and Customer  received data to CustomerDetailsDto. So, we need to create a mapper method for that.
 * - So, inside the CustomerMapper class present inside the mapper package, create a new method like mapToCustomerDetailsDto. It is very similar to mapToCustomerDto, so just replicate it and do the necessary adjustments to reflect our requirements in terms IPO. Its return type will be 'CustomerDetailsDto' and it will take 'Customer' and 'CustomerDetailsDto' as input parameters. And now inside the method, we need to map all the data from the 'Customer' input parameter to 'CustomerDetailsDto' storage object and return the 'CustomerDetailsDto' object.
 *   For the same, we just need to update the reference from, "customerDto".setxxx to "customerDetailsDto".setxxx and also update the return statement to return "customerDetailsDto". With that, our mapper method is ready which will map all the data from the 'Customer' input parameter to 'CustomerDetailsDto' storage object and return the 'CustomerDetailsDto' object. Now, in my 'fetchCustomerDetails' behavior, we need to call this mapper method to map the received data to 'CustomerDetailsDto' storage object and return the 'CustomerDetailsDto' object.
 * - Now using this customerDetailsDto reference object, I can try to set the missing data i.e, AccountsDto - setAccountsDto(-). But in order to set the AccountsDto, first I need to convert the Accounts data object into AccountsDto object. And for that, we already have an AccountsMapper class which is already defined. In this class we have a behavior mapToAccountsDto which takes the inputs 'accounts' data object and 'AccountsDto' storage object/container. So, I can use this method to convert the 'Accounts' data object to 'AccountsDto' object and then set it to the 'customerDetailsDto'.setAccountsDto() method.
 *   Up to now, we have populated the 'customer' and 'accounts' related information into CustomerDetailsDto storage object. We still have to populate the 'loans' and 'cards' related information into CustomerDetailsDto storage object. This is the interesting part now! haha. How do we achieve this? Unfortunately we are not storing that information inside the DB of Accounts ms. We are storing that respective data in other ms's DBs. That's why we need to leverage the feign client interfaces that we have created and using them we need to invoke the Rest Api methods that we have defined inside them and post that we need to catch the response back and store/populate it inside the CustomerDetailsDto storage object.
 * - To invoke the Loans ms, we need use the 'loansFeignClient' reference that we have injected inside this class to invoke a method fetchLoanDetails. To this 'fetchLoanDetails(-)' behavior, we need to pass a mobile number as an input parameter. Just use the one that we received as input parameter to this 'fetchCustomerDetails' behavior. The return type/output from this method is going to be 'ResponseEntity<LoansDto>'. So, we can use the 'getBody()' method to get the 'LoansDto' object, and then we can set it to 'customerDetailsDto'.setLoansDto(). And the same thing will happen for the cards.
 * - With this, all the required information, we have populated inside the CustomerDetailsDto storage object, and we can return the 'customerDetailsDto' object. With this, we have completed all the business logic inside our 'fetchCustomerDetails' behavior of this class.
 * - As a next step, I will go to the CustomerController class. # Check the discussions and changes we will be making there.
 * */
@Service @AllArgsConstructor
public class CustomerServiceImpl implements ICustomerService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private LoansFeignClient loansFeignClient;
    private CardsFeignClient cardsFeignClient;

    /**
     * Fetches customer details based on a given mobile number
     *
     * @param mobileNumber the mobile number of the customer
     * @return the customer details
     */
    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber) {
//         Trying to fetch customer data from the DB based on mobile number, if not found then throw an exception
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
//        Fetching accounts data from the DB based on customer id that we retrieved from the customer table and if not found then throw an exception
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );
        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer,new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

//        With the below line, behind the scenes, my feign client will connect with the Eureka server and try to get the 'loans' service instance details including the address details. Post that It will perform some load balancing logic, and then it will connect to a particular 'loans' service instance and invoke the 'fetchLoanDetails' Rest Api method and finally will return us the response.
        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(mobileNumber);
        customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());

        customerDetailsDto.setCardsDto(cardsFeignClient.fetchCardDetails(mobileNumber).getBody());

        return customerDetailsDto;
    }
}
