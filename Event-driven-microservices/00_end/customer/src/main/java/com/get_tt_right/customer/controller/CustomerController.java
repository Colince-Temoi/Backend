package com.get_tt_right.customer.controller;

import com.get_tt_right.common.dto.MobileNumberUpdateDto;
import com.get_tt_right.customer.constants.CustomerConstants;
import com.get_tt_right.customer.dto.CustomerDto;
import com.get_tt_right.customer.dto.ResponseDto;
import com.get_tt_right.customer.service.ICustomerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class CustomerController {

    private final ICustomerService iCustomerService;

    public CustomerController(ICustomerService iCustomerService) {
        this.iCustomerService = iCustomerService;
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createCustomer(@Valid @RequestBody CustomerDto customerDto) {
        customerDto.setCustomerId(UUID.randomUUID().toString());
        iCustomerService.createCustomer(customerDto);
        return ResponseEntity
                .status(org.springframework.http.HttpStatus.CREATED)
                .body(new ResponseDto(CustomerConstants.STATUS_201, CustomerConstants.MESSAGE_201));
    }

    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> fetchCustomerDetails(@RequestParam("mobileNumber")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    String mobileNumber) {
        CustomerDto fetchedCustomer = iCustomerService.fetchCustomer(mobileNumber);
        return ResponseEntity.status(org.springframework.http.HttpStatus.OK).body(fetchedCustomer);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateCustomerDetails(@Valid @RequestBody CustomerDto customerDto) {
        boolean isUpdated = iCustomerService.updateCustomer(customerDto);
        if (isUpdated) {
            return ResponseEntity
                    .status(org.springframework.http.HttpStatus.OK)
                    .body(new ResponseDto(CustomerConstants.STATUS_200, CustomerConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto(CustomerConstants.STATUS_500,
                            CustomerConstants.MESSAGE_500_UPDATE));
        }
    }

    @PatchMapping("/delete")
    public ResponseEntity<ResponseDto> deleteCustomer(@RequestParam("customerId")
    @Pattern(regexp = "(^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$)",
            message = "CustomerId is invalid") String customerId) {
        boolean isDeleted = iCustomerService.deleteCustomer(customerId);
        if (isDeleted) {
            return ResponseEntity
                    .status(org.springframework.http.HttpStatus.OK)
                    .body(new ResponseDto(CustomerConstants.STATUS_200,
                            CustomerConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto(CustomerConstants.STATUS_500,
                            CustomerConstants.MESSAGE_500_DELETE));
        }
    }

    /** Here the API path I am going to mention as "/mobile-number". The name can be anything but I ma trying to give this name here.
     * Next, I am typing public ResponseEntity which is going to accept ResponseDto inside the body and the method name I am going to mention as "updateMobileNumber". To this method I am going to accept a request body of type MobileNumberUpdateDto. Since I looking to perform validation on the data that I am going to receive inside this object i.e., MobileNumberUpdateDto, I need to mention one more annotation i.e., @Valid.
     * From this method only, I need to write all the logic. For now, I am returning a ResponseEntity object with the status of OK which means that the invocation is successful and inside the body I can try to send the ResponseDto object with status as 200 and message from the Constant MESSAGE_200. If you try to understand what we have inside the ResponseDto object, check the ResponseDto class. We are trying to populate the status code as 200 and the message as MESSAGE_200 - which is "Request Processed successfully".
     * For now, this is a dummy API which is going to return this Dummy response but we are going to enhance it to have some business logic. With what we have done so far you should be crisp clear, now lets write some business logic inside this method. 1st I am going to create an abstract method inside the ICustomerService with the signature of boolean updateMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto); This method I am going to call inside this method and to it I am going to pass the MobileNumberUpdateDto object which I have received as an input.
     * Here I can catch the output using a boolean and based on that, I can try to send various responses to the client. For now let us keep it simple as is. This code should work without any issues. Now, as a next step I should go to the CustomerServiceImpl class and implement the signature that we have from the interface. Check out the docstring of that method for more details.
     * */
    @PatchMapping("/mobile-number")
    public ResponseEntity<ResponseDto> updateMobileNumber(@Valid @RequestBody MobileNumberUpdateDto mobileNumberUpdateDto) {
        iCustomerService.updateMobileNumber(mobileNumberUpdateDto);
        return ResponseEntity.status(org.springframework.http.HttpStatus.OK).
                body(new ResponseDto(CustomerConstants.STATUS_200, CustomerConstants.MESSAGE_200));
    }

}
