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

/** Updates  as on 12/01/2025
 * As if now,we know how to activate a specific profile by hardcoding the profile value inside the application.properties file by using the property spring.profiles.active.
 * With this we have a big disadvantage as everytime we need to move our code from one environment to another environment and activate the profile specific to that environment we need to change the value of the spring.profiles.active property inside the application.properties file.
 * This means we will have to regenerate our docker image/software package everytime we move our code from one environment to another environment which is against to the 15-factor CN principles/methodology.
 * To overcome this challenge, we need to identify if there is any way inside Spring boot where we can activate a specific profile from an external location or through an external parameter.
 * Spring boot provides various ways to externalize our configurations and to activate them. Inside these approaches, the very first and mostly used approach is with the help of Command line arguments.
 * Check slides for more explanation.
 * We will see how to activate a specific profile using externalized configurations approaches like command line arguments, environment variables, and Jvm system properties.
 * Through the IDE itself, we can achieve all this easily. We can provide Command line arguments, Environment variables, and Jvm system properties through the IDE itself.
 * We don't have to go through a long process of, 1st generating a jar file, then running the jar file with the help of java -jar command and then passing the command line arguments, environment variables, and Jvm system properties as discussed in the slides. No need!
 * Such kind of approach can be used by the operations as well as the platforms team where they are trying to run the application through the CI/CD tools like Jenkins, GitHub actions, etc.
 * Since we are developers and have access to the IDE, lets try to utilize the same.
 * We will see how to activate a specific profile using command line arguments. Since we are already using an IDE, by using this IDE its very simple, haha. How?
 * To provide command line arguments,
 * 1. Go to you Spring boot application main class and right click on it.
 * 2. More Run/Debug >> Modify Run Configuration ...
 * 3. A window will open. Under the Modify option dropdown >> Select Program Arguments. >> A text field will come up where you can provide the command line/program arguments.
 * 4. Like we discussed in the slides, you need to provide a prefix 2 hyphens -- and then the property key and value. i.e., --spring.profiles.active=prod --build.version=1.1
 * 5. Click on Apply and then OK.
 * Now run the application. You will see the production profile is activated and the overridden build version is printed in the postman console as output when you invoke the respective APIs.
 * For example, on invoking the /build-info API, you will see the overridden build version is printed in the postman console as output. Not the one present in the application_prod.yaml file.
 * If you invoke the contact-info API, accounts contact information will be printed in the postman console as output i.e.,  i.e., message, ' ...prod APIs'. The Product Owner name and also the mobile numbers mentioned inside the prod profile.
 * This way, we can happily activate a specific profile through externalized configurations like command line arguments.
 *
 * Now, lets try to use the next approach which is Jvm System Properties.
 * To provide Jvm System Properties,
 * 1. Go to you Spring boot application main class and right click on it.
 * 2. More Run/Debug >> Modify Run Configuration ...
 * 3. A window will open. Under the Modify option dropdown >> Select Add VM options. >> A text field will come up where you can provide the Jvm system properties.
 * 4. Like we discussed in the slides, you need to provide a prefix -D and then the property key and value. i.e., -Dspring.profiles.active=qa -Dbuild.version=1.3
 * 5. Click on Apply and then OK.
 * Now run the application. You will see the qa profile is activated and the overridden build version is printed in the postman console as output when you invoke the respective APIs.
 * For example, on invoking the /build-info API, you will see the overridden build version is printed in the postman console as output. Not the one present in the application_qa.yaml file.
 * If you invoke the contact-info API, accounts contact information will be printed in the postman console as output i.e.,  i.e., message, ' ...qa APIs'. The QA Lead name and also the mobile numbers mentioned inside the qa profile.
 * This way, we can happily activate a specific profile through externalized configurations like Jvm System Properties.
 * Note: Make sure to delete the command line arguments that you have provided in the previous step. Because, if you don't delete them, they will be considered and the Jvm system properties will not be considered due to precedence order.
 * Now, lets try to use the next approach which is Environment Variables.
 * To provide Environment Variables,
 * 1. Go to you Spring boot application main class and right click on it.
 * 2. More Run/Debug >> Modify Run Configuration ...
 * 3. A window will open. Under the Modify option dropdown >> Select Environment Variables. >> A text field will come up where you can provide the Environment Variables.
 * 4. Like we discussed in the slides, you need to provide the property key and value. i.e., SPRING_PROFILES_ACTIVE=qa;BUILD_VERSION=1.8
 * 5. Click on Apply and then OK.
 * Now run the application. You will see the qa profile is activated and the overridden build version is printed in the postman console as output when you invoke the respective APIs.
 * For example, on invoking the /build-info API, you will see the overridden build version is printed in the postman console as output. Not the one present in the application_qa.yaml file.
 * If you invoke the contact-info API, accounts contact information will be printed in the postman console as output i.e.,  i.e., message, ' ...qa APIs'. The QA Lead name and also the mobile numbers mentioned inside the qa profile.
 * This way, we can happily activate a specific profile through externalized configurations like Environment Variables.
 * Note: Make sure to delete the Jvm System Properties that you have provided in the previous step. Because, if you don't delete them, they will be considered and the Environment Variables will not be considered due to precedence order.
 * Now, lets try to provide the build.version property in all the 4 approaches and see which one is going to take the precedence. I.e., the profile configuration file, command line arguments, Jvm System Properties, and Environment Variables.
 * As per our understanding, the precedence order is, command line arguments > Jvm System Properties > Environment Variables > profile configuration file.
 * Now when you run the application, you will see the overridden build version configured in the Command line arguments is printed in the postman console as output when you invoke build-info API.
 * If I remove the command line arguments, this time the fight will be between Jvm System Properties and Environment Variables. The one which is provided in the Jvm System Properties will be taken into consideration.
 * If I remove both the command line arguments and the Jvm System Properties, the one which is provided in the Environment Variables will be taken into consideration.
 * If I remove both the command line arguments, the Jvm System Properties, and the Environment Variables, the one which is provided in the profile configuration file will be taken into consideration.
 * This way, we are able to solve most of our problems, we built different profiles inside our application, we are able to activate them through externalized configurations like command line arguments, Jvm System Properties, and Environment Variables.
 * At the same time we can override an existing property or can provide a new property through externalized configurations and this way we can make our microservices immutable and the same docker image we can deploy to multiple environments without the need of regenerating the docker image/software package everytime we move our code from one environment to another environment.
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
