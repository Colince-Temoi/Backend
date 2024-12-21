package com.eazybytes.controller;

import com.eazybytes.model.Accounts;
import com.eazybytes.model.Customer;
import com.eazybytes.repository.AccountsRepository;
import com.eazybytes.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/** Explanation of changes made in this file compared to the previous version
 * As of now, we can see that the APIs inside our controllers are accepting the customer id as an input.
 * With the previous setup that we had, this type of input may work fine without any issues because first the Angular application will try to authenticate the end-user by providing username and pwd.
 * And to the Angular UI application we used to return/send the customer details which includes the customer id as well.
 * The same customerId value, the angular application used to send for all the subsequent APIs and that's why things would work just fine in such a scenario.
 * But now since the APIs are going to be invoked by 3rd party applications it is not going to make sense if we as the 3rd party applications to send the customer id as an input.
 * For them customer id is not going to make any sense as this id is specific to our DB and that's why instead of int:id we are going to accept the String:email as an input.
 * With the help of this email value, we are going to fetch the customer details from our DB, and then using the received customer details object we need to retrieve the customer id and populate it to the accountsRepository.findByCustomerId() method which will help us get the customer's account details.
 * And the same we need to return back to the 3rd party/client application as an output.
 * To achieve all this logic, first we injected CustomerRepository and AccountsRepository into our controller class.
 * In this was we have changed the api to accept the email as an input and then we are fetching the customer details based on the email.
 * The same kind of change I've also made inside the CardsController, LoansController, and BalanceController classes.
 * */

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountsRepository accountsRepository;
    private final CustomerRepository customerRepository;

    @GetMapping("/myAccount")
    public Accounts getAccountDetails(@RequestParam String email) {
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);
        if (optionalCustomer.isPresent()) {
            Accounts accounts = accountsRepository.findByCustomerId(optionalCustomer.get().getId());
            if (accounts != null ) {
                return accounts;
            }else {
                return null;
            }
        }else {
            return null;
        }

    }

}