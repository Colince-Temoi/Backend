package com.get_tt_right.accounts.controller;

import com.get_tt_right.accounts.dto.CustomerDetailsDto;
import com.get_tt_right.accounts.dto.ErrorResponseDto;
import com.get_tt_right.accounts.service.ICustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/** Update as of 27/3/2025
 * Implementing Cross-Cutting Concerns Tracing and Logging
 * ----------------------------------------------------------
 * + fetchCustomerDetails(mobileNumber):ResponseEntity<CustomerDetailsDto> currently takes a mobile number as input parameter which is coming as a request parameter, we need to modify REST API method to accept one more input that is going to come as part of the request header.For this we need to leverage @RequestHeader annotation and as input to it, we need to mention <header_name>:String i.e., @RequestHeader("eazybank-correlation-id") Type placeholder, So whatever value we have in this header, we need to assign it to a method parameter/placeholder with the name  correlationId and is of type String.
 * With this in place our REST API method will now have access to the correlation id inside the request header that is being sent by my gateway server. Now, I am going to create few loggers inside this controller and using those loggers and this correlationId value we will print some logger statements which will help us during the debugging. For the same we have defined an slf4j logger property for this class. Using the logger reference, we are going to create some logger statements inside my fetchCustomerDetails method. Before the method executing starts, it will first print the correlation id. When this log,logger.debug("EazyBank_correlation-id found: {}", correlationId);, prints on the console, it will be having:
 *   - the class name
 *   - The statement which we are trying to print which includes the correlation id
 * After levering this correlation id inside my Accounts ms, I want to forward the same to my Loans and Accounts ms as well. For the same, we need to pass this correlationId as a second parameter to the fetchCustomerDetails method present inside the iAccountsService interface.
 * You can check the CustomerService, CustomerServiceImpl, CardsFeignClient and LoansFeignClient classes for the changes we have done.
 * With this, all the changes we are to do inside the accounts ms is completed. As a next step, we need to make sure the fetch API's available inside the cards and loans ms's are also accepting the request header with the name eazybank-correlation-id.
 * Check out these the CardsController and LoansController classes fetch api's for more details.
 * */
@Tag(
        name = "REST APIs for Customers in EazyBank",
        description = "REST APIs in EazyBank to FETCH customer details"
)
@RestController
@RequestMapping(path="/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class CustomerController {

    private static Logger logger = LoggerFactory.getLogger(CustomerController.class);
    private final ICustomerService iCustomerService;

    public CustomerController(ICustomerService iCustomerService) {
        this.iCustomerService = iCustomerService;
    }

    @Operation(
            summary = "Fetch Customer Details REST API",
            description = "REST API to fetch Customer details based on a mobile number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping("/fetchCustomerDetails")
    public ResponseEntity<CustomerDetailsDto> fetchCustomerDetails(
            @RequestHeader("eazybank-correlation-id") String correlationId,
            @RequestParam
            @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
            String mobileNumber) {
        logger.debug("eazyBank_correlation-id found: {}", correlationId);
        CustomerDetailsDto customerDetailsDto = iCustomerService.fetchCustomerDetails(mobileNumber, correlationId);

        return ResponseEntity.status(HttpStatus.SC_OK).body(customerDetailsDto);
    }
}
