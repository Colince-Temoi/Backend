package com.get_tt_right.accounts.service.client;

import com.get_tt_right.accounts.dto.CardsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/** Update as of 5/3/2025
 * The purpose of @FeignClient(-) annotation is to tell my feign client to connect with the Eureka Server at runtime, and it will try to get all the cards ms instances with a logical name 'cards'.
 * Inside this interface, we are creating a new Abstract method i.e., . Whatever Abstract method that we are going to create inside this interface, its signature has to match with the actual REST API method that we have defined inside the Cards Ms. If you go to the CardsController, we have a method with the name fetchCardDetails. I have to create a very similar method inside this interface with the same signature. You can copy it and paste it here. Don't copy the implementation, just the signature.
 * But inside this interface we are not going to write any implementation code. So, make the method abstract by adding a semicolon at the end. Once we have defined this Abstract method, we can remove the validations related code i.e., @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits") because the validations will be performed at the REST API level but not here at the feign client level. If you clearly observe, the abstract method, fetchCardDetails,  that we have defined here is following the same method name as the REST API method that we have defined inside the CardsController.
 *  It is worth noting that the method name inside this feign client interface can be whatever name you want but make sure that the method signature matches with the actual REST API method that we have defined inside the Cards Ms. By signature I mean: The method input parameters (Except Validations kind of stuff/annotation definations) i.e.,(@RequestParam String mobileNumber) , the method return type i.e., ResponseEntity<CardsDto> and the method access modifier i.e., public. Once this Abstract method definition is done, you can notice that we don't have this CardsDto inside Accounts ms, this implies we have to create it inside Accounts ms. To make work easier just copy it from the Cards ms dto package and paste it here inside the Accounts ms dto package.
 *  Very similarly, we also need LoansDto as well because we are going to develop a new feign client interface for the Loans Ms. So, copy it from the Loans ms dto package and paste it here inside the Accounts ms dto package. With this, we have copied all the required DTOs inside the Accounts ms dto package. Now, you can be able to resolve the dependency issue on CardsDto inside this feign client interface. With this now, we have declared the abstract method. On top of this Abstract method, we need to mention the REST API path details that my actual Rest API method inside the CardsController is going to handle/support. You can see that it is: @GetMapping("/fetch") Make sure to copy and mention this REST API path details here on top of the Abstract method. This path details has to be fully qualified path details and so make sure to add/prefix this with the specified path that is specific to the entire CardsController if it is there. In this case we have something like: @RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE}) in our CardsController
 *  So make sure to copy the path '/api' and prefix it to what we are specifying in top of our defined abstract method below. Up to now we should be good, but if needed we can also mention the expected input type i.e., consumes = {MediaType.APPLICATION_JSON_VALUE}). Like this, we are trying to communicate to the feign client that, the api that we are going to call that is present inside the Cards Ms is expecting the input type to be JSON. If you clearly inspect the Abstract method, CardsFeignClient that we have defined below, it matches with the REST API method that we have defined inside the CardsController. This is one of the Primary rules that we need to make sure we are following.
 * - On top of that the another primary rule is, we need to make sure we are mentioning the interface level annotation like @FeignClient(name = "cards") that is present inside CardsFeignClient interface. This annotation takes the logical name of the Cards Ms as an argument/input. Behind the scenes my CardsFeignClient will connect with the Eureka Server and try to fetch all the instances that are registered with the logical name 'cards'. Once it receives the 'n' instances details of Cards ms, it will try to cache those details for 30 seconds which is the default period. Within these 30 seconds, it will not try to connect with the Eureka Server again and will use the cached details. So, based upon the Ip details inside the cache, it is going to invoke the api i.e., /api/fetch that is present inside the Cards Ms along with the request input(s) which is the Mobile Number in this case. Behind the scenes, all the implementation code will be generated by the feign client. Here you can see that we have not written any business logic, we are just telling to our feign client how to connect to other microservice by providing/defining the necessary definitions as discussed extensively above.
 *   I.e., What is the method signature, what are/is the request parameter, what is the request data structure i.e., Json, What is the response structure, what is the Rest API path, ..etc. If you define all these meta details then the remaining magic will be taken care by the feign client.
 * - Now, lets create a very similar feign client for the Loans Ms. Just copy this CardsFeignClient and reproduce it for the Loans Ms Feign Client interface. Just make the necessary adjustments. Just like we have discussed above. With these changes, we should be good to leverage this Feign Client interfaces inside our Accounts Ms to interact with the other ms's like Cards and Loans ms's. With this, you should be clear with the steps we have followed till now.
 * - As of now, we made the required changes inside the Accounts ms to connect with the Cards and Loans ms's. We have created LoansFeignClient and CardsFeignClient interfaces. As a next step, we want to use them inside our Accounts ms to communicate with the other ms's like Cards and Loans ms's and we should accept the response and we should consolidate all the response from the 3 ms's like Cards, Loans and Accounts ms's and send the same to the client application. Since we want to send the consolidated response from accounts, loans and cards ms's , we need to make sure we create a DTO supporting this consolidated response. For the same, inside my Accounts ms dto package, we need to create a new DTO with the name: CustomerDetailsDto. It is going to hold all the details about a customer like Accounts, Loans, Cards along with their personal details.
 *   . Instead of typing all the dto fields for CustomerDetailsDto, we can just copy them from the CustomerDto since all the details we are going to send inside the CustomerDetailsDto is very similar to what we have inside this CustomerDto. So coy the @Data and @Schema class level definitions and paste that to the CustomerDetailsDto class. The @Schema name property value I will mention as CustomerDetails and the @Schema description property value I will mention as, 'Schema to hold Customer, Accounts, Cards and Loans Information' Now, get/copy the Customer details from the CustomerDto and paste it to the CustomerDetailsDto. With this you should see customer related information placeholders/fields like name, email, mobile number and what are his/her accounts details that we are representing in the form of AccountsDto secondary placeholder. Very similarly, we should mention the missing information placeholders i.e., We need secondary placeholders for cards and loans information in the form of CardsDto and LoansDto. It is simple, just copy the AccountsDto placeholder definition twice and make the necessary changes to reflect CardsDto and LoansDto. With this, the CustomerDetailsDto will have all the required placeholders to hold all the required details that we are going to send to the client application.
 *    +. Check out this CustomerDetailsDto class for more discussions.
 * */
@FeignClient(name = "cards")
public interface CardsFeignClient {
    @GetMapping(value = "/api/fetch", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CardsDto> fetchCardDetails(@RequestParam String mobileNumber);
}
