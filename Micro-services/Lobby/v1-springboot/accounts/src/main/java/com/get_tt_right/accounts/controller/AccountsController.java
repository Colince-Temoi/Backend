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

/** Updates  as on 07/01/2025
 *As of now we have made some configurations inside the application.yml file of this Accounts service. The challenge we are facing right now is, if you try to deploy this code into various environments then the very same configuration properties are the ones the service is going to use.
 * What if we have a requirement to use different configuration properties in different environments? This is a very often requirement inside real projects. For example, take DB credentials related configuration properties. They should not have the same values inside all the environments.
 * Based upon the environment, we need to use different DB credentials. How do we overcome this challenge and how will Spring boot help us in this scenario?
 * Inside Spring boot, we have a beautiful concept called profiles. Check slide for more information regarding this concept.
 * Now, lets try to implement profiles concept inside our Accounts service. Just like we have application.yaml file create 2 different files, application_qa and application_prod yaml files under the resources folder. application.yaml belongs to the default profile, application_qa.yaml belongs to the qa/testing profile and application_prod.yaml belongs to the prod profile.
 * Inside the application.yaml file, we have may properties i.e, port related configurations, build version, contact information, etc. First, you have to identify which configurations/properties are going to change from environment to environment inside your microservice. The server.port is going to be the same for all the environments. We don't have to start our application
 * in on different ports in different environments. That's why we don't need to move this property into the other profiles. We can have this as a default value which will always bee loaded by the Spring boot framework. Very similarly, since we are using h2 database and it is going to be the same for all the environments, we can always keep this configuration inside the application.yaml file. This is inclusive of
 * spring.application.name, spring.datasource.url, spring.datasource.driverClassName, spring.datasource.username, spring.datasource.password, spring.jpa.hibernate.ddl-auto, spring.h2.console.enabled, spring.jpa.show-sql, spring.jpa.database-platform, spring.jpa.hibernate.ddl-auto, spring.jpa.show-sql, ...etc. All these configurations are going to be the same for all the environments. So, we don't need to move them into the other profiles.
 * But I have a requirement tah only the build version, accounts related configurations are going to be different from environment to environment inside my microservice. So, we need to move these configurations into the other profiles. Just create/copy these similar set of properties inside the application_qa.yaml and application_prod.yaml files but we need to make sure that these properties have different values in different profiles because that is our intention/requirement otherwise there is no need of creating different profiles.
 * Before I try to copy these properties into the other profiles, I need to make some changes to the value in the default profiles. I will change the value of the build.version to 3.0 in the default profile. Which means, inside my local development, the version that we have deployed right now is 3.0. But in the QA environment, the version is going to be 2.0 and in the production environment, the version is going to be 1.0. So, I will make these changes in the other profiles.
 * This way we are mentioning different values inside different profiles. Check the respective yaml files to see the changes that I have made and what I am talking about.
 * Once we make the changes, we should be good from the perspective of the 2 yaml files i.e., application_qa.yaml and application_prod.yaml. As a next change, we should tell to the Spring boot framework that we have created 2 different profiles and these are the names of the profiles/yaml files. For the same we need to define the property called spring.config.import inside the application.yaml file. Under the import, I need to mention the list of yaml files that I have created.
 * Now we have created all the required profiles and mentioned the same inside the application.yaml file to import them. As a next step, we should activate at least one profile. If you are not activating any profile then by default the default profile is going to be activated. First with whatever changes we have lets try to test the default profile. So, I will run the application and then invoke the APIs contact-info and build-info to verify that the default profile is activated.
 * Yeey! we are getting the output values from the default profile. That's why you are able to see in the postman response that the build version is 3.0 and the contact information is also from the default profile i.e., message, ' ...local APIs'. The developer name and also the mobile numbers mentioned inside the default profile.
 * For java version endpoint, you will always see the same output because you are running your microservice in the same local system. Now, I want to activate the UAT/QA or Production profile, how do I do that? Go to the application.yaml file and configure the property spring.profiles.active. This property takes a list of profile(s) that you want to activate.Since I have the requirement to activate only the QA profile, I will mention the profile name as qa. It will be a single element in the list.
 * The same value that you mentioned in your profile yaml file i.e., application_qa.yaml when configuring the property spring.config.activate.on-profile, you need to mention the same value when configuring the property spring.profiles.active inside the application.yaml file in order to activate qa profile. Based on this, spring boot is going to activate all the properties present inside the application_qa.yaml file.
 * We know that, the default profile is going to be activated if we don't do the configurations just discussed above and now we are trying to activate a profile which is qa. Spring boot will see the same property key names and for any key name that is also mentioned inside the qa profile yaml file, it is going to override the value of that key name with the value that is present inside the qa profile yaml file otherwise it is going to take the value from the default profile.
 * Now, I will run the application and then invoke the APIs contact-info and build-info to verify that the qa profile is activated. Yeey! we are getting the output values from the qa profile. That's why you are able to see in the postman response that the build version is 2.0 and the accounts contact information is also from the qa profile i.e., message, ' ...QA APIs'. The QA Lead name and also the mobile numbers mentioned inside the qa profile. This way, we can happily activate a specific profile.
 * Suppose I want to activate the production profile inside production environment, then that means I need to come to the application.yaml file inside my code and change the value of the property spring.profiles.active to prod. This way, I can activate the production profile but I will have to re-generate my docker image and deploy it into the production environment. This doesn't make sense because we know very well that our artifact should be immutable and by doing any slightest change like we have done and re-generating a software package for each environment again and again is not a good practice and breaches the immutability concept.
 * So, how to overcome this challenge?  We have several options that we can leverage to change the property values dynamically during the start of an application through an external parameter.
 * By now, you should be clear on how to create multiple profiles and how to activate a particular profile. Next, we will discuss, the other options that we have to activate a specific profile though an external parameter.
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
