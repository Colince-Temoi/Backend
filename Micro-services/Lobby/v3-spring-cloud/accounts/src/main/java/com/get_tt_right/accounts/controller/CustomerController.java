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

/** Update as of 21/6/2025
 * As of now, we are trying to generate the logs and tyring to append the correlationId in the log that we are receiving from the gateway server application. Instead of this, we are going to introduce new logger statements i.e., logger.debug("Fetch Customer Details Method Start");. To this Logger statement,my OpenTelemetry at runtime  it is going to inject metadata information - Application name,Trace ID and Span ID. With that reason, we no more need to manually append the correlationId that we are receiving in the headers of the request from the gatewayserver application in the log(s).
 * Also, before the return statement mention  a logger i.e., logger.debug("Fetch Customer Details Method End");. This way, you can add any number of logs inside your ms's at the controller layer, service layer and repository layer. It's up to you where you want to define the log statements. By default, all the logs will have distributed tracing information based upon the pattern that we have mentioned/configured inside the application.yml file of the respective ms's.
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
        logger.debug("Fetch Customer Details Method Start");
        CustomerDetailsDto customerDetailsDto = iCustomerService.fetchCustomerDetails(mobileNumber, correlationId);
         logger.debug("Fetch Customer Details Method End");
        return ResponseEntity.status(HttpStatus.SC_OK).body(customerDetailsDto);
    }
}
