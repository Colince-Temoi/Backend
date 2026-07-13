package com.get_tt_right.customer.function;

import com.get_tt_right.common.dto.MobileNumberUpdateDto;
import com.get_tt_right.customer.service.ICustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
public class CustomerFunctions {

    /**Inside this method business logic, I am not going to trigger any DB changes.
     * We are just simply going to add a logger message for now. But in real applications, there are unlimited possibilities on what you can do by knowing the overall status of the entire business flow i.e, Mobile number updation or any other kind of updation/txn flow that you are going to perform in this Choreography Saga Pattern.
     * When a developer sees this logger, then it is a confirmation that the whole txn flow of the Saga pattern is completed successfully.
     * */
    @Bean
    public Consumer<MobileNumberUpdateDto> updateMobileNumberStatus() {
        return (mobileNumberUpdateDto) -> {
            log.info("Received  updateMobileNumberStatus request  for the details: {}", mobileNumberUpdateDto);
        };
    }

    /** As of now, inside this method, we are simply logging the request details. But what we have do is, we have to actually execute a DB txn to roll back to the previous old mobile number.
     * That's why we are injecting the ICustomerService as a dependency to this function. The rest other details you know.
     * */
    @Bean
    public Consumer<MobileNumberUpdateDto> rollbackCustomerMobileNumber(ICustomerService iCustomerService) {
        return (mobileNumberUpdateDto) -> {
            log.info("Received  rollbackCustomerMobileNumber request  for the details: {}", mobileNumberUpdateDto);
            iCustomerService.rollbackMobileNumber(mobileNumberUpdateDto);
        };
    }

}