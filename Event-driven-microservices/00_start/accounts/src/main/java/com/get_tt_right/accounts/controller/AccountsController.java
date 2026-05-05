package com.get_tt_right.accounts.controller;

import com.get_tt_right.accounts.constants.AccountsConstants;
import com.get_tt_right.accounts.dto.AccountsDto;
import com.get_tt_right.accounts.dto.ResponseDto;
import com.get_tt_right.accounts.service.IAccountsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Here we have multiple API's to create, fetch, update and delete account details.
 * The create api will help us to create a brand-new account inside our DB.
 * Fetch api will help us to load the account details from the db based on a given mobile number. Based on that given mobile number we are doing some validations also - like the mobile number that we receive has to be a 10 digit numerical value.
 * Update api will help us to update the account details inside our DB.
 * Delete api will help us to soft-delete the account details from our DB.
 * All the respective business logic we have written inside the service impl layer - check that out.
 */

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class AccountsController {

    private final IAccountsService iAccountsService;

    public AccountsController(IAccountsService iAccountsService) {
        this.iAccountsService = iAccountsService;
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccount(@RequestParam("mobileNumber")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber) {
        iAccountsService.createAccount(mobileNumber);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
    }

    @GetMapping("/fetch")
    public ResponseEntity<AccountsDto> fetchAccountDetails(@RequestParam("mobileNumber")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    String mobileNumber) {
        AccountsDto accountsDto = iAccountsService.fetchAccount(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(accountsDto);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateAccountDetails(@Valid @RequestBody AccountsDto accountsDto) {
        boolean isUpdated = iAccountsService.updateAccount(accountsDto);
        if (isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto(AccountsConstants.STATUS_500,
                            AccountsConstants.MESSAGE_500_UPDATE));
        }
    }

    @PatchMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAccountDetails(@RequestParam("accountNumber")
    Long accountNumber) {
        boolean isDeleted = iAccountsService.deleteAccount(accountNumber);
        if (isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto(AccountsConstants.STATUS_500,
                            AccountsConstants.MESSAGE_500_DELETE));
        }
    }

}
