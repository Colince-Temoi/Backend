package com.get_tt_right.cards.controller;

import com.get_tt_right.cards.constants.CardsConstants;
import com.get_tt_right.cards.dto.CardsContactInfoDto;
import com.get_tt_right.cards.dto.CardsDto;
import com.get_tt_right.cards.dto.ErrorResponseDto;
import com.get_tt_right.cards.dto.ResponseDto;
import com.get_tt_right.cards.service.ICardsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**Update as of 27/3/2025
 * Implementing Cross-Cutting Concerns Tracing and Logging
 * ----------------------------------------------------------
 *  For the fetchCardDetails method, we have introduced @RequestHeader("eazybank-correlation-id") String correlationId as a parameter.
 *  As a next step, we need to leverage this correlation input value and try to log some statements inside this CardsController class's fetchCardDetails method. For the same we have defined an slf4j logger property for this class. Using the logger reference, we are going to create some logger statements inside my fetchCardDetails method.
 *  We can mention the same log statement that we mentioned inside the CustomerController class's fetchCustomerDetails method here.
 *  Before the method executing starts, it will first print the correlation id. When this log,logger.debug("EazyBank_correlation-id found: {}", correlationId);, prints on the console, it will be having:
 *    - the class name
 *    - The statement which we are trying to print which includes the correlation id
 * With this, we have done all the Java Changes, as a next step, inside the application.yml file of GatewayServer, we have defined/configured the debugging logging  for the package com.get_tt_right.gwserver. Very similarly, we have to provide such kind of configuration for the other ms's. As of now we have written log statement inside Loans/Cards and Accounts ms's and therefore, we have to provide such kind of configuration for the other ms's otherwise if you don't provide this configuration that enables logging of our log statements inside the other ms's, you will not be able to see the log statements inside the other ms's getting printed on the console or wherever you are re-directing the logs.
 *For the same, open the application.yml file of my accounts/loans/cards ms's and at the end of the file, add a new property called logging.level.com.get_tt_right.* and set it to DEBUG. In place of * you have to provide the correct parent/root package name of the ms's.
 * With this, we are done with all the required changes. Now, restart all the services starting with the configserver, eurekaserver, accounts, loans and cards ms's. Once my cards, accounts and loans ms's have completely started successfully and registered with the EurekaServer then only I am going to restart my gatewayserver application. If you visit the eureka dashboard now, you should be able to see all my ms's registered with the EurekaServer. i.e., accounts, loans and cards ms's including even the gatewayserver ms. Once this is done, we can try to validate all the changes done.
 * - Clean the console of loans, cards and accounts ms's along with the gatewayserver ms startup console logs and try to see the log statements getting printed on the console when you make a request to FetchCustomerDetails in our Postman collection. This is so that we can be able to see our own custom log statements that we have defined inside our custom filters. To test this, we will be invoking the fetchCustomerDetails request in our Postman collection present inside the gatewayserver folder.  This is going to send a request to the fetchCustomerDetails Rest API method present inside the Accounts ms CustomerController class by providing an input mobile number.
 *   http://localhost:8072/eazybank/accounts/api/fetchCustomerDetails?mobileNumber=4354437687. Since we don't have any data inside loans cards and accounts ms's, we will need to create it first. The reason we don't have that is because we were using a h2 db. That's why let's try to create the data inside the accounts, cards and loans ms's. Under the gatewayserver I have created respective requests that will help us achieve all these. The first request that we are firing with the help of gatewayserver is http://localhost:8072/eazybank/accounts/api/create, then followed by http://localhost:8072/eazybank/cards/api/create?mobileNumber=0768531317 and finally http://localhost:8072/eazybank/loans/api/create?mobileNumber=0768531317
 *   As the next step, lets invoke the fetchCustomerDetails request in the Postman collection present inside the gatewayserver folder. This will give us the output that we are expecting. If you go and check the response headers section, you will see there are 2 custom headers that we added in our discussions i.e., eazybank-correlation-id and X-Response-Time
 *   My external client applications can use this correlation id / trace id in future whenever they want to reach out to the EazyBank saying that " For so-and-so request with so-and-so correlation/trace id something is wrong, please debug" In the support ticket that they will raise to EazyBank, they can mention this correlation/trace id. Using this correlation id, my developer can try to analyze the log statements. Lets put ourselves in the shoe of this developer and try to debug:
 *   +. First we will go to the GatewayServer console. If you try to search for the same trace id i.e., 6aa551b7-ae86-4de1-99bf-509889e2890a, you will be able to see 2 log statements i.e.,
 *      2025-03-29T15:14:25.735+03:00 DEBUG 21744 --- [gatewayserver] [ctor-http-nio-1] c.g.gwserver.filters.RequestTraceFilter  : eazyBank-correlation-id generated in RequestTraceFilter : 6aa551b7-ae86-4de1-99bf-509889e2890a
 *      2025-03-29T15:14:30.544+03:00 DEBUG 21744 --- [gatewayserver] [ctor-http-nio-1] c.g.g.filters.ResponseTraceFilter        : Updated the correlation id to the outbound headers: 6aa551b7-ae86-4de1-99bf-509889e2890a
 *  The first one is while we are trying to generate the trace/correlationid inside the RequestTraceFilter class and the second one is inside the ResponseTraceFilter class while we are trying to create a new custom header inside the response with the same trace/correlation id as value that we are going to send to our external client applications. This confirms that my gatewayserver has no issues while creating the correlation id and sending it to my external client applications.
 *  Now let me go into the Accounts application logger statements. If you try to search for the same trace id i.e., 6aa551b7-ae86-4de1-99bf-509889e2890a, you will be able to see 1 log statements i.e.,2025-03-29T15:14:26.645+03:00 DEBUG 15688 --- [accounts] [nio-8080-exec-4] c.g.a.controller.CustomerController      : eazyBank_correlation-id found: 6aa551b7-ae86-4de1-99bf-509889e2890a
 *  This confirms that the request reached to the accounts ms. In the scenarios where is didn't reach, then definitely you can't find this trace id related logs inside loans and cards ms's. But since in our scenario on making the request to fetchCustomerDetails and received a successful response as expected, we should be able to see the logger statements with the given trace id inside the accounts, cards as well as loans application.
 *  - With all this in mind, I hope you can visualize the power of gateway server which is acting as an edge server. We have just seen one simple business logic for our demo. But in real projects you may have complex business logic also. For example, You can implement any kind of cross-cutting concerns like auditing, security, logging, etc. inside your edge server with the help of these filters available inside the edge server. We will later on discuss microservices security and there, we will leverage the gateway server to implement the security so that only authenticated and authorized external client applications/users can be able to reach out to our internal ms's in the ms's network.
 * Up to now, you should have clarity on what we have discussed so far about the gateway server.
 * Also for a quick reference, check out the slides on all the important steps that we need to follow while trying to implement this gateway server with the help of this Spring Cloud Gateway.
 * Also check out a commit with the message, "Implementing Cross-Cutting Concerns Tracing and Logging" for mere details on what we have just discussed above.
 */

@Tag(
        name = "CRUD REST APIs for Cards in EazyBank",
        description = "CRUD REST APIs in EazyBank to CREATE, UPDATE, FETCH AND DELETE card details"
)
@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
//@AllArgsConstructor
@Validated
public class CardsController {

    private static Logger logger = LoggerFactory.getLogger(CardsController.class);
    private ICardsService iCardsService;

    public CardsController(ICardsService iCardsService) {
        this.iCardsService = iCardsService;
    }

    @Value("${build.version}")
    private String buildVersion;

    @Autowired
    private Environment environment;

    @Autowired
    private CardsContactInfoDto cardsContactInfoDto;

/**
 *
 * @param mobileNumber - the 10-digit mobile number of the customer for whom the card is being created.
 *                       Must match the pattern specified.
 * @return ResponseEntity containing a ResponseDto object with status code and message.
*/
    @Operation(
            summary = "Create Card REST API",
            description = "REST API to create new Card inside EazyBank"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
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
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createCard(@Valid @RequestParam
                                                      @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                      String mobileNumber) {
        iCardsService.createCard(mobileNumber);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ResponseDto(CardsConstants.STATUS_201, CardsConstants.MESSAGE_201));
    }


    @Operation(
            summary = "Fetch Card Details REST API",
            description = "REST API to fetch card details based on a mobile number"
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
    })
/**
 * @param mobileNumber - the 10-digit mobile number of the customer whose card details are being fetched.
 *                       Must match the pattern specified.
 * @return ResponseEntity containing a CardsDto object with card details.
 */
    @GetMapping("/fetch")
    public ResponseEntity<CardsDto> fetchCardDetails( @RequestHeader ("eazybank-correlation-id") String correlationId,
                                                      @RequestParam @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                               String mobileNumber) {
        logger.debug("EazyBank_correlation-id found: {}", correlationId);
        CardsDto cardsDto = iCardsService.fetchCard(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(cardsDto);
    }

    @Operation(
            summary = "Update Card Details REST API",
            description = "REST API to update card details based on a card number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
        })
/**
 *
 * @param cardsDto - The CardsDto object containing the card details to be updated.
 *                   It must include valid card number, card type, mobile number, total limit,
 *                   amount used, and available amount.
 * @return ResponseEntity containing a ResponseDto object with status code and message
 *         indicating if the update was successful or if there was an error.
 */
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateCardDetails(@Valid @RequestBody CardsDto cardsDto) {
        boolean isUpdated = iCardsService.updateCard(cardsDto);
        if(isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(CardsConstants.STATUS_200, CardsConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(CardsConstants.STATUS_417, CardsConstants.MESSAGE_417_UPDATE));
        }
    }

    @Operation(
            summary = "Delete Card Details REST API",
            description = "REST API to delete Card details based on a mobile number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
/**
 * @param mobileNumber - the 10-digit mobile number of the customer whose card details are being deleted.
 *                       Must match the pattern specified.
 * @return ResponseEntity containing a ResponseDto object with status code and message
 *         indicating if the deletion was successful or if there was an error.
 */
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteCardDetails(@RequestParam
                                                                @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                                String mobileNumber) {
        boolean isDeleted = iCardsService.deleteCard(mobileNumber);
        if(isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(CardsConstants.STATUS_200, CardsConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(CardsConstants.STATUS_417, CardsConstants.MESSAGE_417_DELETE));
        }
    }

    @Operation(
            summary = "Get Build information",
            description = "Get Build information that is deployed into cards microservice"
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
    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(buildVersion);
    }

    @Operation(
            summary = "Get Java version",
            description = "Get Java versions details that is installed into cards microservice"
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
    @GetMapping("/java-version")
    public ResponseEntity<String> getJavaVersion() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(environment.getProperty("JAVA_HOME"));
    }

    @Operation(
            summary = "Get Contact Info",
            description = "Contact Info details that can be reached out in case of any issues"
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
    @GetMapping("/contact-info")
    public ResponseEntity<CardsContactInfoDto> getContactInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cardsContactInfoDto);
    }


}
