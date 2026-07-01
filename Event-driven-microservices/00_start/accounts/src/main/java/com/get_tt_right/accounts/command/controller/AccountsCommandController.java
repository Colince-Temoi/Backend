package com.get_tt_right.accounts.command.controller;

import com.get_tt_right.accounts.command.CreateAccountCommand;
import com.get_tt_right.accounts.command.DeleteAccountCommand;
import com.get_tt_right.accounts.command.UpdateAccountCommand;
import com.get_tt_right.accounts.constants.AccountsConstants;
import com.get_tt_right.accounts.dto.AccountsDto;
import com.get_tt_right.accounts.dto.ResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

/** Inside this controller we are going to have 3 different operations supporting creat, update and delete.
 * Inside the respective APIs, we are trying to create we are trying to create and populated the respective command objects and then using the CommandGateway we are trying to dispatch these commands to the Axon framework and the Axon Server.
 * */
@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@RequiredArgsConstructor
public class AccountsCommandController {
    private final CommandGateway commandGateway;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccount(@RequestParam("mobileNumber")
                                                     @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber) {
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);
        CreateAccountCommand createCommand = CreateAccountCommand.builder()
                .accountNumber(randomAccNumber).mobileNumber(mobileNumber)
                .accountType(AccountsConstants.SAVINGS).branchAddress(AccountsConstants.ADDRESS)
                .activeSw(AccountsConstants.ACTIVE_SW).build();
        commandGateway.sendAndWait(createCommand);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateAccountDetails(@Valid @RequestBody AccountsDto accountsDto) {
        UpdateAccountCommand updateCommand = UpdateAccountCommand.builder()
                .accountNumber(accountsDto.getAccountNumber()).mobileNumber(accountsDto.getMobileNumber())
                .accountType(accountsDto.getAccountType()).branchAddress(accountsDto.getBranchAddress())
                .activeSw(AccountsConstants.ACTIVE_SW).build();
        commandGateway.sendAndWait(updateCommand);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
    }

    @PatchMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAccountDetails(@RequestParam("accountNumber") Long accountNumber) {
        DeleteAccountCommand deleteCommand = DeleteAccountCommand.builder()
                .accountNumber(accountNumber).activeSw(AccountsConstants.IN_ACTIVE_SW).build();
        commandGateway.sendAndWait(deleteCommand);
        return ResponseEntity
                .status(org.springframework.http.HttpStatus.OK)
                .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
    }
}
