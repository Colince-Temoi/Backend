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

/** Updates  as on 05/01/2025
 * We will update this microservice by defining som properties inside the application.yaml file and try to read them using the approaches that we have discussed in our slides.
 * We 1st created a new folder in our workspace location named v1-springboot. Here, V1 stand s=for version 1 and here we will be discussing all the basic approaches provided by Spring boot framework to read the properties/configurations. Later we will have a look into other advanced approaches.
 * In this folder, I just copied all the 3 microservices that we have created in our previous sessions. I copied the accounts, customers and cards microservices.
 * Next, open this folder in your IDE. I am using IntelliJ IDEA. Make sure to click on Load maven scripts so that all our microservices can be detected as maven projects.
 * If you recall, we discussed 3 different approaches to generate a docker image from our microservices, and we finalized with the decision to use Google Jib plugin to generate the docker images throughout.
 * For this reason, I will make some changes inside my Accounts and Loans microservices to use the Google Jib approach instead of Dockerfile and Build packs respectively. Check with the respective pom.xml files for these changes.
 * In the Accounts microservice, first make sure to delete the Dockerfile. Next, open the pom.xml file and add the Google Jib plugin configuration. Copy it from the cards' microservice pom.xml file under the plugins section.
 * Inside loans microservice, under the plugins section, add the Google Jib plugin configuration as well and remember to delete the spring-boot-maven-plugin configuration that we were using to generate the docker image.
 * Next, open the application.yaml file and add the properties that we want to read in our Accounts microservice. I have added the properties for the build version and more others. Check it out.
 * To read the build version property value, 1st create a new field, buildVersion, with type as String inside the AccountsController class. It will be a wise decision to follow the same name inside your Java fields so that you can easily match it with the property key.
 * At the time of defining it, it will have a default value - null. To inject the property value during the startup of the spring boot application inside this java field, Next, Annotate it with @Value annotation. To this annotation, we need to pass the key of the property that we want to read by following the Spring Expression Language - SPEL.
 * The syntax that we need to follow is first mention the double quotes inside which mention a dollar sign followed by parenthesis and then mention the property key - build.version. This annotation will help us to read the value of the property from the application.yaml file.
 * Now build a REST API to expose this property value/send this build information to the client. I will create a new REST API with the path as /build-info. This REST API will return the build version of the accounts' microservice.
 * Now you can run the accounts' microservice and hit the REST API /build-info to get the build version of the accounts' microservice. Hurey! You have successfully read the property value from the application.yaml file using the @Value annotation.
 * You may face a CE like 'Could not autowire. No beans of 'String' type found.' The solution to this is:
 *  - Make sure to comment out the @@AllArgsConstructor class level annotation.
 *  - Manually create a constructor for the fields that you are trying to inject in this class by passing the parameters to it.
 * The reason for the issue first of all is that, by using this @AllArgsConstructor annotation, we are telling the spring boot framework to create a constructor for all the fields that are present in this class. But, the spring boot framework is not able to find any bean of type String in my application to inject into this class. So, it is throwing this error.
 * This is the reason why we need to comment out this annotation and manually create a constructor for the fields that we are trying to inject in this class.
 * With the help of the constructor we have created,we are trying to autowire only one dependency bean of type IAccountsService. This is the reason why we are not facing any issues with this dependency injection.
 * It is also a recommended approach to mention final keyword before the field that you are trying to inject in this class while following constructor injection. This will help you to avoid any accidental changes to the field value. This will make your application more secure.
 * And if needed, you can also mention @Autowired annotation on to of that constructor but this is completely optional when we have a single constructor in our class.
 *
 * So, this is how we can read the property values from the application.yaml file using the @Value annotation in our spring boot application. But this is not a recommended approach when working with 100s of microservices and if they have 100 different properties to read. I mean are you going to create 100 different properties inside your Java class and annotate them with @Value annotation? No, right? This is not a recommended approach/viable solution.
 * At the same time inside this approach, while reading the property related values, we are hardcoding the property key name like build.version. This is not a recommended approach because if the property key name changes in the future, then we need to change it in all the places where we are using it. This is not a good practice.
 * This approach you can use only if you have 1 or 2 properties to read. But just don't use it either way.
 *
 * Another approach to read the properties, especially environment properties that are defined in the environment where you have deployed your microservice. You may have a question like, why do we need to define some properties as environment variables? Why can't we define them directly inside the application.yaml file? This is a very valid question.
 * Like I said before, some sensitive information like Passwords we can't define inside the application.yaml file where they will be exposed. With that reason it is always advisable to define the sensitive configuration details as environment variables only so that no one can see those values because they will not have access to your production server. Only the Server Admins will have access to it. This way you are trying to secure your application whenever you are dealing with sensitive configurations.
 * Another reason is, when you deploy your microservice in different environments like Dev, QA, UAT, Prod, etc, you may have different configurations for each environment. For example, you may have different database URLs, different usernames, passwords, etc. So, to handle these different configurations, we need to define them as environment variables.
 * This approach involves an interface with the name Environment available in the org.springframework.core.env package. First, we are autowiring this Environment interface inside our class. With this, all the environment details that I have defined inside my local system can be accessed with the help of this Environment interface reference.
 * To demo this, I have created a REST API with the path as /java-version. This REST API will return the JAVA_HOME environment variable value that is defined in my local system. This is the Java Version that my microservice is using right now.
 * Inside your local system or inside any Java developer system, we will always install Java and set the environment variable with the name JAVA_HOME. This environment variable will have the path where the Java is installed in your system. This is the reason why we are trying to read this environment variable value in our microservice.
 * With the help of Environment interface reference that we have autowired, we are trying to read the JAVA_HOME environment variable value by invoking the getProperty method and passing the key as JAVA_HOME. This we are returning to the client who tries to invoke this REST API. This is how we can read the environment variable values in our spring boot application.
 * You can also read any other environment variable values that are defined in your local system or in the environment where you have deployed your microservice. For example, M2_HOME, PATH, MAVEN_HOME, etc. You can read any of these environment variable values using the Environment interface reference.
 * We will get a response as the location where the Java is installed in our local system. Ideally, inside prod servers we will directly install the JDK by installing from the official website and in that scenario, it is going to give you the complete folder location where the Java is installed along with the Java version name.
 * Inside my local system I also have Maven installed. So, I can also read the MAVEN_HOME environment variable value using the Environment interface reference. This is how we can read the environment variable values in our spring boot application.
 * With this you should be clear about this second approach to read the properties/configuration in our spring boot application. This is a recommended approach when you are dealing with environment variables. It is also worth noting that with this approach, we cannot read properties defined inside the application.yaml file. This approach is only for environment variables.
 * This approach also has disadvantages like, you can only read one property at a time, and at the same time you need to hard code the property key name like JAVA_HOME, MAVEN_HOME, etc. This is not a recommended approach when you have 100s of properties to read. It will only work if you have 1 or 2 properties to read.
 *
 * The 3rd approach is with the help of @ConfigurationProperties annotation. This is the most recommended approach when you are dealing with 100s of properties to read with just a single Pojo class. All the previous limitations that we have discussed in the previous 2 approaches will be resolved with this approach.
 * The limitations were like hardcoding the property key name inside the Java code, reading only one property at a time, etc. To demo this approach, I will first configure a set of properties/configurations that are required by my Accounts microservice. Whenever we are trying to use this @ConfigurationProperties approach,
 * we need to make sure all our configurations/properties have a common prefix name. This is a mandatory requirement. For the same I am going to create several properties inside the application.yaml file with the prefix as accounts. Check this out in the application.yaml file.
 *
 * In a nutshell, we've seen demo of how to map multiple properties from the application.yaml file to a Pojo class using the @ConfigurationProperties annotation. Since this is a more mature approach, Spring team recommends to use this approach compared to the @Value annotation.
 * Of course, we need to use the Environment interface approach to read the environment variable values as in real-time applications we will not be having many environment configurations. They may be like 2 or 3. But for the rest normal configurations and properties they will be many and that's why using this 3rd approach is going to be more convenient to use.
 *
 * Q. We have created an application.yaml file and have defined several configurations in it. What if I want to have different values for different environments? Maybe I want to follow whatever I have defined now inside my dev environment and I want to have different values for the same properties in my QA/prod environment. How do I achieve that? This is a very valid question.
 * If my requirement is to maintain different values for different environments then definitely all the 3 approaches that we have discussed so far are not going to be helpful as whatever is in question now is something more advanced that is supported by the spring-boot framework.
 * The spring boot framework provides a very advanced feature called Profiles. This feature will help us to maintain different values for different environments. In order to use this feature, we need to create different application.yaml files for different environments. This we will explore next!
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
                .body(environment.getProperty("MAVEN_HOME"));
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
     * This is a record class that we have created to map all the properties that we have defined inside the application.yaml file.
     * Now inside the body of this method, we are returning the entire object of AccountsContactInfoDto record class.
     * This way, you can create 100s of properties inside application.yaml and for all of them you can define Java fields inside a Dto class - Whether you are following a Record class or a normal Pojo class. It is completely up to you.
     * But please make sure the field names in the Pojo/Record class should match with the property key names that you have defined inside the application.yaml file. Also, the Return data types of the fields should also match with the property value data types that you have defined inside the application.yaml file.
        * This is how we can read the properties from the application.yaml file using the @ConfigurationProperties annotation.
     * Run the accounts' microservice and hit the REST API /contact-info to get the contact information. This information can be used by my client application in case of any issues.
     * Here we are just passing the dto object directly in the response but in real projects its up-to you on how to read the data and utilize it present in the dto object.
     * */
    @GetMapping("/contact-info")
    public ResponseEntity<AccountsContactInfoDto> getContactInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountsContactInfoDto);
    }

}
