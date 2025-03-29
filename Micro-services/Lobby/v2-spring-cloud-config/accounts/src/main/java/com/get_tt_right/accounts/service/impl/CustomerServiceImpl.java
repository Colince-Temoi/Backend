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

/** Update as of 27/3/2025
 * We have introduced a correlation id in the service layer as input to the fetchCustomerDetails method. This same correlation id we need it be sent to the Loans and Cards ms as well. Where are we trying to invoke the loans and cards ms's? We are trying to do this with the help of loansFeignClient and cardsFeignClient. So, while we are trying to invoke the respective feign client methods, we need to pass this correlation id as a first parameter.
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
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber,String correlationId) {
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
        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(correlationId,mobileNumber);
        customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());

        customerDetailsDto.setCardsDto(cardsFeignClient.fetchCardDetails(correlationId,mobileNumber).getBody());

        return customerDetailsDto;
    }
}
