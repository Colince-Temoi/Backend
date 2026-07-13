package com.get_tt_right.accounts.functions;

import com.get_tt_right.accounts.service.IAccountsService;
import com.get_tt_right.common.dto.MobileNumberUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;
/** Inside this class we can create a new function/method public then Consumer because we want to create a function that adopts the Consumer functional interface since we are going to build logic which only accepts input but not publish any output. My function is going to accept ONLY MobileNumberUpdateDto object as an input. Followed by we need to mention the method name which should be same as the definition name that you have given in your application.yml file. For now my method is an empty method. Inside this method, basically we need to return a lambda expression which is going to be executed whenever my function is going to be invoked by the Spring cloud Stream. Check the docstring for this method for more details.
 *
 * */
@Configuration
@Slf4j
public class AccountFunctions {

    /** So, we are typing return, followed by a lambda expression with the variable name as mobileNumberUpdateDto.
     * As a next step, you need to amke sure you are mentioning @Bean annotation on top of this function so that this function can be managed by the Spring Cloud Functions behind the scenes.
     * To this function, I am also injecting IAccountsService interface as an input. Since we are using @Bean annotation, this dependency will also be injected by Spring framework during the startup of the application.
     * Now, you can go to the IAccountsService interface and try to create an Abstract method with the signature of boolean updateMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto); - This is very similar to what we did in the Customer ms.
     * Now, from my function we can try to invoke the new abstract method that we have created in the IAccountsService interface and to the same I can try to pass the MobileNumberUpdateDto object which I have received as an input.
     * Just before invoking the updateMobileNumber method, I am adding a log statement confirming "received updateAccountMobileNumber request for the details provided which are inside the MobileNumberUpdateDto object". Since the execution of the flow is very important inside the Saga pattern, that's why we are trying to add these loggers which are going to help us in our debugging.
     * Next, you can go to AccountsServiceImpl class and implement the signature that we have from the interface. Check out the docstring of that method for more details.
     * */
    @Bean
    public Consumer<MobileNumberUpdateDto> updateAccountMobileNumber(IAccountsService iAccountsService) {
        return (mobileNumberUpdateDto) -> {
               log.info("Received  updateAccountMobileNumber request  for the details: {}", mobileNumberUpdateDto);
                iAccountsService.updateMobileNumber(mobileNumberUpdateDto);
        };
    }

    @Bean
    public Consumer<MobileNumberUpdateDto> rollbackAccountMobileNumber(IAccountsService iAccountsService) {
        return (mobileNumberUpdateDto) -> {
            log.info("Received  rollbackAccountMobileNumber request  for the details: {}", mobileNumberUpdateDto);
            iAccountsService.rollbackMobileNumber(mobileNumberUpdateDto);
        };
    }

}