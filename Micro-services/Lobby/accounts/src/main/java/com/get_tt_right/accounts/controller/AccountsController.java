package com.get_tt_right.accounts.controller;

import com.get_tt_right.accounts.constants.AccountsConstants;
import com.get_tt_right.accounts.dto.CustomerDto;
import com.get_tt_right.accounts.dto.ResponseDto;
import com.get_tt_right.accounts.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** The Controller layer is only responsible to accept the requests,perform any validations if needed and then pass the request to the service layer. Finally, it will return the response back to the client.
 *
 * */

@RestController // Instructing Spring boot that we are going to write methods with annotations related to the HTTP methods. Therefore, accordingly expose the methods as REST APIs to the outside world.
@AllArgsConstructor // Lombok's annotation to generate a constructor with all the arguments. It will be used to inject the dependencies. In this case, it will inject the IAccountsService dependency.
@RequestMapping(path="/api", produces = {MediaType.APPLICATION_JSON_VALUE}) // In real-time applications, it always recommended to have a base/prefix path for all the APIs. So, we are defining the base path here. If needed some of the applications will maintain also a versioning of the APIs. I.e., /api/v1, /api/v2, etc. Apart from this, it is also a good practice mentioning the produces attribute to specify the response type of your APIs.
public class AccountsController {
    private IAccountsService iAccountsService;

    /**
     * API to create a new account.
     * @param customerDto - CustomerDto object, contains the customer details.
     * @return ResponseEntity with the status and message.
     * .status(HttpStatus.CREATED) - This will set the status code as 201.It will go in the response header.
     * .body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201)) - This will set the response body with the status code and message.
     * This is the standard we need to follow with the help of ResponseEntity.
     * Using this ResponseEntity object we can send a lot of information in the response header and body based upon our requirement.
     */
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccount(@RequestBody CustomerDto customerDto) {
        iAccountsService.createAccount(customerDto);
        /* If there is no Exception or issue in the service layer, then we will return the response with status code 201 as shown below.
        * If there is an exception inside the service layer, due to the business exception - CustomerAlreadyExistsException, then definitely it will never reach to my controller layer instead execution will go to the GlobalExceptionHandler class.
        * In the GlobalExceptionHandler class, we have written the logic to handle the CustomerAlreadyExistsException. So, the response will be sent from there.
        * You may have a question what if there is any other RTE due to any other exceptions? This we will handle later
        * */
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
    }

    /**
     * API to fetch account details based on a mobile number.
     *
     * @RequestParam - This annotation is used to bind the request parameter to the method parameter. The param(s) here are query parameters.
     * The value attribute is used to specify the name of the query parameter.
     *
     * @param mobileNumber - The mobile number associated with the account.
     * @return ResponseEntity containing the CustomerDto if found, along with HTTP status 200.
     */
    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> fetchAccountDetails(@RequestParam String mobileNumber) {
        CustomerDto customerDto = iAccountsService.fetchAccount(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(customerDto);
    }

    /**
     * API to update account details.
     *
     * @RequestBody - This annotation is used to mark the method parameter as the body of the request.
     * The CustomerDto object is converted from the request body.
     *
     * @param customerDto - The CustomerDto object containing the account details to be updated.
     * @return ResponseEntity containing ResponseDto with status code 200 if updated, else 500.
     */
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateAccountDetails(@RequestBody CustomerDto customerDto) {
        boolean isUpdated = iAccountsService.updateAccount(customerDto);
        if(isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto(AccountsConstants.STATUS_500, AccountsConstants.MESSAGE_500));
        }
    }

    /**
     * API to delete account details based on a mobile number.
     *
     * @RequestParam - This annotation is used to bind the request parameter to the method parameter.
     * The param here is a query parameter.
     *
     * @param mobileNumber - The mobile number associated with the account to be deleted.
     * @return ResponseEntity containing ResponseDto with status code 200 if deleted, else 500.
     */
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAccountDetails(@RequestParam String mobileNumber) {
        boolean isDeleted = iAccountsService.deleteAccount(mobileNumber);
        if(isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto(AccountsConstants.STATUS_500, AccountsConstants.MESSAGE_500));
        }
    }

}
