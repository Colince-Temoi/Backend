package com.get_tt_right.gwserver.handler;

import com.get_tt_right.gwserver.dto.*;
import com.get_tt_right.gwserver.service.client.CustomerSummaryClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/** On top of this class I am mentioning the annotations i.e., @Component and @RequiredArgsConstructor.
 * To this class, we need to inject the bean of CustomerSummeryClient because using the CustomerSummeryClient bean only, we can invoke actual ms's.
 * Next, we have created a method fetchCustomerSummary which is going to return the Mono of ServerResponse. This method is going to accept ServerRequest as an input. ServerRequest and ServerResponse are very similar to the HttpServletRequest and HttpServletResponse that we see in traditional Servlet-based web applications. Since Spring Cloud Gateway project is build based upon the Reactive Programming, we need to use the ServerRequest to accept the request from the client applications and ServerResponse receive the response and send it back to the client applications.
 * From the ServerRequest reference, we can get the queryParam from the request. The queryParam will be the mobileNumber of the customer in our case. The same query param value I am going to catch inside a variable named mobileNumber of String type.
 * Using this mobilNumber, first, I am going to invoke the method which is fetchCustomerDetails and tho this method I am going to pass the mobileNumber as an input. The output from this method we already know - Mono of ResponseEntity which is going to wrap the CustomerDto object. The return type variable I am just going to keep it as customerDetails.
 * Next, same drill for the other method invocations.
 * Now, at the end of the method, we can't simply return because we have to wait for the 4 operations to complete. Once the operations are completed we want to aggregate all the responses into a single object and the same we want to attach to the body of the ServerResponse. So, let's try to understand how to do the same - to make sure that my thread is waiting for all the above 4 operations to complete, we need to use the zip method available inside the Mono. To this zip method I need to pass all the 4 object details like customerDetails, accountDetails, loanDetails and cardDetails. With this, what is going to happen is - my thread is going to wait until all the 4 objects are completely emitted from the API invocations that we are trying to do parrallely. Once all these objects are available, we can invoke a method which is flatMap. To this flatMap, we are going to get a tuple object. What is a tuple object? A tuple object is one that is capable of holding multiple objects of different data types. Usually inside Collections like List, Set, etc they are only capable of Storing single datatype objects. But inside Reactive Programming, this tuple is capable of holding multiple elements which belongs to various data types. In our case the responses emitted i.e., customerDetails, accountDetails, loanDetails and cardDetails belong to various data types.
 * So, by the time the zip method completes execution - if you can hover on it - we are going to get a Tuple object which is storing 4 different elements i.e., customerDetails, accountDetails, loanDetails and cardDetails. That's why inside this flatMap method implementation logic, using the tuple reference, what we are going to do is - we are going to invoke the method which is getT1 which is going to give me the ResponseEntity object that wraps CustomerDto. From this ResponseEntity of CustomerDto we are going to invoke the getBody method which is going to give us the CustomerDto object.The same we are trying to catch on the LHS with the variable customerDto. Same drill for the other 3 objects. Once we have all the objects, we can simply create a CustomerSummaryDto object with the help of the constructor of CustomerSummaryDto. To this constructor, I am going to pass customerDto, accountsDto, loansDto and cardsDto. At last from this flatMap implementation, we are going to return a ServerResponse object by invoking ServerResponse ok method indicating that the invocation is successful, followed by contentType with the MediaType as APPLICATION_JSON and at last I am going to invoke the body method and to this body method, I am going to pass the code which is BodyInserters.fromValue(customerSummaryDto). As you can see to this fromValue method we are going to pass the object of CustomerSummaryDto.
 * So, as can be seen from this flatMap implementation we are trying to return the ServerResponse object as an output and that same ServerResponse object we want to return as an output to the method fetchCustomerSummary. That's why in front of Mono we need to put a return statement. With all we have discussed now, our handler/method i.e., fetchCustomerSummery is trying to invoke all the 4 ms's parralley in a non-blocking style and the aggregating the responses into a single object.
 * Now, you maybe under the assumption that everything is ready and we can invoke this the handler/method fetchCustomerSummary from the client applications - haha news flash! it is not going to work that way as this is not a REST API - it is just simple a handler method. Inside reactive applications, we can't build REST APIs traditionally with the help of RestController - instead, we need to define routing configurations which are capable of handling a specific API request. That is what we will try to implement next - check with the router package for more details.
 * */
@Component
@RequiredArgsConstructor
public class CustomerCompositeHandler {

    private final CustomerSummaryClient customerSummaryClient;

    public Mono<ServerResponse> fetchCustomerSummary(ServerRequest serverRequest) {
        String mobileNumber = serverRequest.queryParam("mobileNumber").get();

//        With the below 4 lines of code we are invoking 4 different ms's parallely in a non-blocking style. When the 1st line of code is executed my thread is not going to wait for the response of customerDetails instead it is going to simply jump on to the next line of code and in this way, the below 4 lines of code are going to be executed parallely in a non-blocking style.
        Mono<ResponseEntity<CustomerDto>> customerDetails = customerSummaryClient.fetchCustomerDetails(mobileNumber); // At this line of code we are actually invoking the customer ms.
        Mono<ResponseEntity<AccountsDto>> accountDetails = customerSummaryClient.fetchAccountDetails(mobileNumber); // At this line of code we are actually invoking the account ms.
        Mono<ResponseEntity<LoansDto>> loanDetails = customerSummaryClient.fetchLoanDetails(mobileNumber); // At this line of code we are actually invoking the loan ms.
        Mono<ResponseEntity<CardsDto>> cardDetails = customerSummaryClient.fetchCardDetails(mobileNumber); // At this line of code we are actually invoking the card ms.

        return Mono.zip(customerDetails, accountDetails, loanDetails, cardDetails)
                .flatMap(tuple -> {
                    CustomerDto customerDto = tuple.getT1().getBody();
                    AccountsDto accountsDto = tuple.getT2().getBody();
                    LoansDto loansDto = tuple.getT3().getBody();
                    CardsDto cardsDto = tuple.getT4().getBody();
                    CustomerSummaryDto customerSummaryDto = new CustomerSummaryDto(customerDto, accountsDto, loansDto, cardsDto);
                    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromValue(customerSummaryDto));
                });


    }

}