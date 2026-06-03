package com.get_tt_right.customer.query.controller;

import com.get_tt_right.customer.dto.CustomerDto;
import com.get_tt_right.customer.query.FindCustomerQuery;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Now, you can go to the CustomerController class and copy all it's class level annotations i.e., @RestController,@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE}),@Validated and paste them as class level annotations inside the CustomerQueryController class. After these annotations, also mention a lombok class level annotation i.e., @RequiredArgsConstructor.
 * As a next step, we need to build a REST API that is going to support the query/fetch operation. We already have such an API/operation inside the CustomerController class. So, copy the entire fetchCustomerDetails method/endpoint along with its method level annotation(s) from CustomerController class to CustomerQueryController class. To this CustomerQueryController class, you need to inject the ICustomerService interface as a dependency so that we can use the methods available inside the CustomerServiceImpl class(But in the next lines of docstring you will realize we don't need this dependency here).
 * Based on what we had implemented inside the CustomerController class, we are directly trying to invoke the fetchCustomer method available inside the ICustomerService interface impl class. But we should not try to do this when we are implementing the CQRS. We should go/adhere with the flow recommended by the framework as illustrated in our slide. As can be visualized, from the Read APIs, we need to create a Query object and publish it to the Axon framework so that it will go to the QueryHandler where we are going to write all the logic on how to fetch/read the data from the Reader DB. That's the flow/approach that we need to follow as it is going to give more flexibility to the developers to build many query API's/Objects and their respective handlers based upon their business requirements.
 * So, here, as soon as we receive the input mobile number, what we can do first is to try to create the object of FindCustomerQuery class. To the constructor of this class, we need to pass the mobile number as the input parameter and finally, we can publish this FindCustomerQuery object to the Axon framework. How to do that? haha Now, instead of injecting the ICustomerService interface into the CustomerQueryController class, we are going to inject the Axon QueryGateway interface as a dependency. Just like how we have the CommandGateway on the command side, we also have the QueryGateway on the query side. And, we can use the QueryGateway interface to publish the FindCustomerQuery object to the Axon framework. How? We have a query method to which we need to pass the object of FindCustomerQuery. After passing this input parameter, we should also tell to the Axon framework what we are expecting as an output from this query method - that's why using this ResponseTypes class, we need to invoke the instanceOf method and to this we need to pass the CustomerDto.class since we are expecting the output in the form of CustomerDto which will have customerId, name, email, mobileNumber and activeSw - the same is what we need to provide as an input to this instanceOf method.
 * Next we need to invoke the join method which is from the CompletableFuture class - you can check out it's docstring for more details. If you can read the documentation of that method, it says - "Return the result value when complete or throw an exception is completed exceptionally". So, whenever we are trying to invoke this query method, behind the scenes the query operation is going to be invoked asynchronously in a non-blocking style but with the help of chaining its with this join method we are trying to wait for the output so that we can eventually send the same output to the end-user. You can always open this query method which will take you to the QueryGateway Interface and inside this QueryGateway interface, you will be able to find many query related abstract methods using which you can try to query your Reader DB using various styles. As of now, I am trying to use this type discussed, but we are going to discuss other overloaded query methods in the coming sessions. If you read the documentation of any of this query overloaded methods, you should be able to see and visualize some useful information. For example for the signature  which we are currently using you will see a documentation i.e., "Sends given query over the QueryBus, expecting a response in the form of responseType from a single source. The query name will be derived from the provided query. Execution may be asynchronous, depending on the QueryBus implementation." Hope this is making sense to you on how we are going about stuff.
 * On the LHS, I am going to catch the output into a variable called customerDto and using that, return it towards the end by leveraging ResponseEntity.status(HttpStatus.OK).body(customerDto).
 * So with this logic we are nothing but simply publishing the FindCustomerQuery object to the Axon framework using the query method of the QueryGateway interface, but we don't have yet any logic on how this query need to be handled. To implement the business logic related to this query, we need to create a new package under the query with the name 'handler'. Inside this package we need to create a new class called CustomerQueryHandler. Here, we can write the logic on how to fetch/read the data from the Reader DB. Check out the CustomerQueryHandler class for more details.
 * */
@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@RequiredArgsConstructor
public class CustomerQueryController {

    private final QueryGateway queryGateway;
    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> fetchCustomerDetails(@RequestParam("mobileNumber")
                                                            @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
                                                            String mobileNumber) {
        FindCustomerQuery findCustomerQuery = new FindCustomerQuery(mobileNumber);
        CustomerDto customerDto = queryGateway.query(findCustomerQuery, ResponseTypes.instanceOf(CustomerDto.class)).join();
        return ResponseEntity.status(HttpStatus.OK).body(customerDto);
    }
}
