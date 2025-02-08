package com.get_tt_right.accounts.controller;

import com.get_tt_right.accounts.constants.AccountsConstants;
import com.get_tt_right.accounts.dto.AccountsContactInfoDto;
import com.get_tt_right.accounts.dto.CustomerDto;
import com.get_tt_right.accounts.dto.ErrorResponseDto;
import com.get_tt_right.accounts.dto.ResponseDto;
import com.get_tt_right.accounts.service.IAccountsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/** Updates  as on 15/01/2025
 * This way, we are able to solve most of our problems, we built different profiles inside our application, we are able to activate them through externalized configurations like command line arguments, Jvm System Properties, and Environment Variables.
 * At the same time we can override an existing property or can provide a new property through externalized configurations and this way we can make our microservices immutable and the same docker image we can deploy to multiple environments without the need of regenerating the docker image/software package everytime we move our code from one environment to another environment.
 * Do you think that this is the best approach, and we can follow it? Yes! this may work to some extent. You will be seeing some projects or some organisations using this Spring profiles concept and externalized configurations concept to maintain their configurations and properties inside the microservices environment.
 * This is the most basic approach, to maintain the configurations and properties inside the microservices environment/CN applications. But this may not work to some extent as this approach has some good amount of disadvantage which we will discuss next! But before we try to discuss them, lets make the similar changes inside cards and loans microservices as well.
 * Check the class level documentations of cards and loans microservice controller class level documentations for more information.
 * */

@Tag(
        name = "CRUD REST APIs for Accounts in EazyBank",
        description = "CRUD REST APIs in EazyBank to CREATE, UPDATE, FETCH AND DELETE account details"
)
@RestController
//@AllArgsConstructor
@RequestMapping(path="/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated // This annotation will tell to my spring boot framework to perform validation on all the REST APIs that I have defined in this AccountsController.
public class AccountsController {

    private final IAccountsService iAccountsService;

    @Autowired
    private Environment environment;

    @Autowired
    private AccountsContactInfoDto accountsContactInfoDto;
    public AccountsController(IAccountsService iAccountsService) {
        this.iAccountsService = iAccountsService;
    }

    @Value("${build.version}")
    private String buildVersion;

    @Operation(
            summary = "Create Account REST API",
            description = "REST API to create new Customer &  Account inside EazyBank"
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
    public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto) {
        iAccountsService.createAccount(customerDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
    }

    @Operation(
            summary = "Fetch Account Details REST API",
            description = "REST API to fetch Customer &  Account details based on a mobile number"
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
    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> fetchAccountDetails(@RequestParam
                                                               @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                               String mobileNumber) {
        CustomerDto customerDto = iAccountsService.fetchAccount(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(customerDto);
    }

    @Operation(
            summary = "Update Account Details REST API",
            description = "REST API to update Customer &  Account details based on a account number"
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
    }
    )
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateAccountDetails(@Valid @RequestBody CustomerDto customerDto) {
        boolean isUpdated = iAccountsService.updateAccount(customerDto);
        if(isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_UPDATE));
        }
    }

    @Operation(
            summary = "Delete Account & Customer Details REST API",
            description = "REST API to delete Customer &  Account details based on a mobile number"
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
    }
    )
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAccountDetails(@RequestParam
                                                                @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                                String mobileNumber) {
        boolean isDeleted = iAccountsService.deleteAccount(mobileNumber);
        if(isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_DELETE));
        }
    }

    @Operation(
            summary = "Get Build information",
            description = "Get Build information that is deployed into accounts microservice"
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
            description = "Get Java versions details that is installed into accounts microservice"
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
    /** Any client that invokes this REST API, can get the contact information.
     * In order to write the business logic inside this method, first we need to make sure we are injecting the bean of AccountsContactInfoDto record class.
     * This is a record class that we have created to map all the properties that we have defined inside the application.yml file.
     * Now inside the body of this method, we are returning the entire object of AccountsContactInfoDto record class.
     * This way, you can create 100s of properties inside application.yml and for all of them you can define Java fields inside a Dto class - Whether you are following a Record class or a normal Pojo class. It is completely up to you.
     * But please make sure the field names in the Pojo/Record class should match with the property key names that you have defined inside the application.yml file. Also, the Return data types of the fields should also match with the property value data types that you have defined inside the application.yml file.
        * This is how we can read the properties from the application.yml file using the @ConfigurationProperties annotation.
     * Run the accounts' microservice and hit the REST API /contact-info to get the contact information. This information can be used by my client application in case of any issues.
     * Here we are just passing the dto object directly in the response but in real projects its up-to you on how to read the data and utilize it present in the dto object. This is just a Demo on how to map multiple properties from application.yml to a single Pojo class and that's why we have used this 3rd approach which is @ConfigurationProperties annotation.
     * Since this is a more mature approach, Spring team recommends the usage of this approach compared to @Value annotation.
     * Of course, we need to use Environment interface to read the environment configurations. But in real applications, we will not be having multiple environment configurations/properties. They will be handful, just 2 or 3.
     * Normal properties and configurations that you may need to configure may be multiple/many and that's why using this 3rd approach which is @ConfigurationProperties annotation is recommended and is going to make more sense using.
     * As of now, we have explored all the 3 approaches but here I have a question for you. As of now, we have created an application.yaml, and we have defined all the properties. What if I want to have different values for different environments?
     * Maybe I want to follow, whatever values I have defined inside the application.yml file for the development environment and I want to have different values for the QA environment and production environment. How do I achieve this?
     * If this is my requirement, to maintain different values for different environments, then definitely all the 3 approaches we have discussed above are not going to be helpful. We need something advanced that is supported by the Spring boot framework.
     * Check the class level documentation of this class to see how we can achieve this requirement.
     * */
    @GetMapping("/contact-info")
    public ResponseEntity<AccountsContactInfoDto> getContactInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountsContactInfoDto);
    }

}
