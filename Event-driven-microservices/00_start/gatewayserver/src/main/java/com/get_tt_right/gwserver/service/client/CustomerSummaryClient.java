package com.get_tt_right.gwserver.service.client;


import com.get_tt_right.gwserver.dto.AccountsDto;
import com.get_tt_right.gwserver.dto.CardsDto;
import com.get_tt_right.gwserver.dto.CustomerDto;
import com.get_tt_right.gwserver.dto.LoansDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Mono;

/** Here first we are going to mention the details around the Customer ms. First we are mentioning the @GetExchange annotation and to it we are going to mention the value attribute with an input as the path that my gatewayserver can use to invoke the customer ms. '/eazybank/customer' is a prefix and 'api/fetch' is the actual path that we have developed inside the customer ms. After this we should also mention what is the media type that the api in customer ms is going to accept/take by using the accept attribute.
 * Below that annotation we are going to define an abstract method signature with the method name being 'fetchCustomerDetails' and this is going to accept a RequestParam of type String with the param name as 'mobileNumber'. The return type from this method should be Mono of ResponseEntity and inside the response entity we want to accept a CustomerDto object because the Customer ms is responsible to provide only the customer related information. Here we are trying to use Mono as a return type - if you are not clear about the reactive programming model let's in the next line understand the purpose of Mono.
 * As you know Spring Cloud Gateway project is build based upon the reactive programming model just to make sure that it handles a large number of requests with less number of threads and memory. Inside Reactive programming model, we have 2 different concepts - one is Mono and the other one is Flux. Whenever you are expecting a single object as a response then you can use Mono whereas if you are expecting multiple/collection of objects as a response then you can use Flux. In our case since we are expecting only a single CustomerDto object as a response then we can use Mono. With this Mono, what we are trying to tell to the Spring Reactive Programming model is - we want to invoke the given/mentioned/defined API asynchronously in a non-blocking style. As soon as that API is invoked behind the scenes, the thread will not wait for the response, the thread will move on to the next piece of code.
 * This way we can parallely try to invoke all the ms's involved and towards the end, we can try to wait for the responses from all the ms's and compose the same. We are going to discuss how to invoke parallelly in a few. Same drill, define abstract method signatures for accounts, loans and cards ms's. With all this, what my gatewayserver can do whenever it wants to invoke any of the ms's (customer, accounts, loans or cards) is - it can simply leverage these abstract method signatures and behind the scenes all the magic of invoking the actual ms's is going to happen.
 * */
public interface CustomerSummaryClient {

    @GetExchange(value= "/eazybank/customer/api/fetch", accept = "application/json")
    Mono<ResponseEntity<CustomerDto>> fetchCustomerDetails(@RequestParam("mobileNumber") String mobileNumber);

    @GetExchange(value= "/eazybank/accounts/api/fetch", accept = "application/json")
    Mono<ResponseEntity<AccountsDto>> fetchAccountDetails(@RequestParam("mobileNumber") String mobileNumber);

    @GetExchange(value= "/eazybank/loans/api/fetch", accept = "application/json")
    Mono<ResponseEntity<LoansDto>> fetchLoanDetails(@RequestParam("mobileNumber") String mobileNumber);

    @GetExchange(value= "/eazybank/cards/api/fetch", accept = "application/json")
    Mono<ResponseEntity<CardsDto>> fetchCardDetails(@RequestParam("mobileNumber") String mobileNumber);

}