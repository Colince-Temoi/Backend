package com.get_tt_right.profile.query.projection;

import com.get_tt_right.common.event.AccountDataChangedEvent;
import com.get_tt_right.common.event.CardDataChangedEvent;
import com.get_tt_right.common.event.CustomerDataChangedEvent;
import com.get_tt_right.common.event.LoanDataChangedEvent;
import com.get_tt_right.profile.service.IProfileService;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

/** First, on top of this class we need to mention an annotation which is @Component because we want a bean of this class to be created by the Spring framework. Followed by we are going to mention another class level annotation which is @RequiredArgsConstructor. This annotation is coming from the Lombok library.
 * MV impl
 * -------
 * Inside this class, 1st we are going to write a method "on" which is going to handle the event of type CustomerDataChangedEvent. Inside this method only I am going to write the business logic but b4 that on top of this method we need to mention a method level annotation which is @EventHandler.
 * From this method, I am going to invoke a method available inside the ProfileServiceImpl class. But 1st what I have to do is, I need to create an abstract method or rather a rule inside the IProfileService interface - the rule is going to have the signature of void handleCustomerDataChangedEvent(CustomerDataChangedEvent customerDataChangedEvent); Now, inside the ProfileServiceImpl class, I am going to override this method - nothing but implement the abstract method from the interface - Check out it's docstring for details.
 * Now, here using the iProfileService variable, I am going to invoke the handleCustomerDataChangedEvent method and to this method I am going to pass the CustomerDataChangedEvent object as an input.
 * Same drill - we need to create logic for the Accounts, Loans and Cards ms's. So, in the iProfileService I am going to copy the abstract method void handleCustomerDataChangedEvent(CustomerDataChangedEvent customerDataChangedEvent); and replicate it like 3 times, and change the signatures accordingly to handle respective events. Next, make the necessary changes inside the ProfileServiceImpl class as well - you can check out the docstring for more details. With this, you should be crisp clear with all the changes we have done inside the ProfileServiceImpl class.
 * Next, here inside the ProfileProjection class, we need to replicate the on method EventHandler method 3 more times and make the necessary changes to handle the respective events.
 * */
@Component
@RequiredArgsConstructor
@ProcessingGroup("customer-group")
public class ProfileProjection {

    private final IProfileService iProfileService;

    @EventHandler
    public void on(CustomerDataChangedEvent customerDataChangedEvent) {
        iProfileService.handleCustomerDataChangedEvent(customerDataChangedEvent);
    }
    @EventHandler
    public void on(AccountDataChangedEvent accountDataChangedEvent) {
        iProfileService.handleAccountDataChangedEvent(accountDataChangedEvent);
    }

    @EventHandler
    public void on(LoanDataChangedEvent loanDataChangedEvent) {
        iProfileService.handleLoanDataChangedEvent(loanDataChangedEvent);
    }

    @EventHandler
    public void on(CardDataChangedEvent customerDataChangedEvent) {
        iProfileService.handleCardDataChangedEvent(customerDataChangedEvent);
    }
}
