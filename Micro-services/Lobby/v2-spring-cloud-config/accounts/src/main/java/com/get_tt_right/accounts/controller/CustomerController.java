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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Update as of 7/3/2025
 * Inside this controller, I am going to create a new RestApi fetchCustomerDetails.
 * This Method will take the input of a mobile number and will return the customer details dto. On top of this mobile number we are going to do some regex validations and make sure that the mobile number is 10 digits. To this method we are also mentioning some Open Api specification related annotations. You can copy this from what we have inside AccountsController and Make the necessary changes.
 * Under @ApiResponses,we are going to mention 200  and 500 which we are going to send in case of success and failure scenario.
 * For the business logic, we are going to leverage the service layer. For this we need an interface and an impl class. i.e., iCustomerService and CustomerServiceImpl. Inside iCustomerService we are going to define the rules/ Abstract method(s) definitions.
 * After we have completed writing the Service layer definitions and business logic we can move on to the controller layer. In this CustomerController, inject the iCustomerService object reference and create a constructor for it to initialize the object and using this object reference call the fetchCustomerDetails method.
 *  Since there is only one constructor inside this class, we don't have to mention @Autowired annotation on top of this constructor. Now Using this 'iCustomerService' object reference, we can call the fetchCustomerDetails method. The logic is going to be very simple that we are going to write inside this 'fetchCustomerDetails' method of this class.
 *   1. By using the 'iCustomereService' object reference, call the 'fetchCustomerDetails' method and pass the mobile number as an input parameter. The received input, I am going to catch it into CustomerDetailsDto object.
 *   2. Return the same to the client application with the help of ResponseEntity object.
 *      To this ResponseEntity object I am going to set several things like; status. After invoking this status method, fluently invoke the body method and pass the CustomerDetailsDto object as its input parameter.
 * - With this, we have made all the required changes. You can do a clean build and try to start all of our ms's . In the order, configserver, eurekaserver, accounts, cards and loans. You can see that all of them are up and running. You can go to the Eureka dashboard just to make sure that all my ms's registered their details with the EurekaServer. Yes! you should see that all my ms's are currently registered with Eureka.
 * - As a next step to test the things up:
 *   1. Make sure you are creating some data inside accounts, cards and loans ms's with the same mobile number. Like you know, we are using h2 DB and that's why we need to create the data again. Also, if you had your respective docker DB containers deleted then you have to create them again and create the data inside them again.
 *      To create the data, just use postman to invoke the CREATE APIs for Accounts, Cards as well as Loans ms's. Note: The phone number has to be the same in all the ms's. This is going to be super quick and simple. Nothing fancy here.
 *   2. Once this is done, if you go to your postman still, under the Accounts folder, you have a request with the name: 'FetchCustomerDetails'. It supports Http GET method, and the path is 'http://localhost:8080/api/fetchCustomerDetails?mobileNumber=4354437687'. The path parameter is the same mobile number that you had created inside the Accounts, Cards and Loans ms's. Now, just invoke this request and you will get the customer details.
 *    If I try invoking this request, behind the scenes, my Accounts ms is going to connect with the loans and cards ms and will give me a consolidated response of all the customer details. Yeey! inside the response, there is Customer, Accounts, Loans as well as Cards related information all under the umbrella of CustomerDetailsDto object.
 * - The beauty here is: We didn't hardcode the API or DNS names anywhere inside Accounts ms. We have just given what is the logical name that other ms's are going to use to register themselves with the EurekaServer. So whenever my accounts ms is trying to connect with other ms's, it will go check with the service discovery Agent which is Eureka. Like, "Heey Eureka! I am trying to connect with loans ms. Can you please give me the details of loans ms so that I can connect with it."
 *   EurekaServer will be like, "Okay buddy! please wait for few seconds, let me check my registry details and post that EurekaServer is going to provide all the instance details of loans ms along with their IP information to my accounts ms." If Accounts ms receives multiple instance details of loans ms then behind the scenes, my feign client is going to use Spring Cloud LoadBalancer and after performing the load balancing strategy, feign client is going to invoke one of the instance of loans or cards ms.
 * - Behind the scenes there is a lot of work going on and all this is being taken care by the feign client and EurekaServer. We as developers, we don't have to worry about this. We have to worry about writing some small configurations, and feign client interfaces and that's it. And with that, the communication between ms's is happening perfectly.
 *   With this, you should be clear with all the stuff that we have discussed so far.
 * */
@Tag(
        name = "REST APIs for Customers in EazyBank",
        description = "REST APIs in EazyBank to FETCH customer details"
)
@RestController
@RequestMapping(path="/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class CustomerController {
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
    ResponseEntity<CustomerDetailsDto> fetchCustomerDetails( @RequestParam
            @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
            String mobileNumber) {
        CustomerDetailsDto customerDetailsDto = iCustomerService.fetchCustomerDetails(mobileNumber);

        return ResponseEntity.status(HttpStatus.SC_OK).body(customerDetailsDto);
    }
}
