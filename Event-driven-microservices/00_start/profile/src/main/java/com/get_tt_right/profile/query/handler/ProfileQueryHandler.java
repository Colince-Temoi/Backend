package com.get_tt_right.profile.query.handler;

import com.get_tt_right.profile.dto.ProfileDto;
import com.get_tt_right.profile.query.FindProfileQuery;
import com.get_tt_right.profile.service.IProfileService;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

/** On top of this class we are adding the @Component and @RequiredArgsConstructor annotations. To this class I need to inject the ICustomerService interface as a dependency so that I can use the methods available inside the CustomerServiceImpl class.
 * Next, we are writing a method which is going to return CustomerDto as an output and the method name we are going to mention as findCustomer. To this method we need to pass the input parameter as FindCustomerQuery object. Then inside this method, using the iCustomerService interface, we are going to invoke the fetchCustomer method and pass the mobile number as the input parameter which we are picking from the FindCustomerQuery object.
 * The output from this method we can straight away return as. But here, there is a catch, on top of this findCustomer method, we need to add the @QueryHandler annotation. Now this method is going to handle this query object of type FindCustomerQuery where we are trying to load the customer details based upon the given mobile number from the read DB.
 * */
@Component
@RequiredArgsConstructor
public class ProfileQueryHandler {
    private final IProfileService iProfileService;

    @QueryHandler
    public ProfileDto findProfile(FindProfileQuery findProfileQuery) {
        return iProfileService.fetchProfile(findProfileQuery.getMobileNumber());
    }
}
